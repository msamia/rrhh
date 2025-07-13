package ar.org.hospitalcuencaalta.servidor_para_monitoreo.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MicroserviceManager {
    private final Map<String, String> serviceDirs;
    private final Map<String, List<Process>> processes = new ConcurrentHashMap<>();

    public MicroserviceManager() {
        serviceDirs = Map.ofEntries(
            Map.entry("servicio-contrato", "../servicio-contrato"),
            Map.entry("servicio-empleado", "../servicio-empleado"),
            Map.entry("servicio-entrenamiento", "../servicio-entrenamiento"),
            Map.entry("servicio-nomina", "../servicio-nomina"),
            Map.entry("servicio-orquestador", "../servicio-orquestador"),
            Map.entry("servicio-consultas", "../servicio-consultas"),
            Map.entry("API-gateway", "../API-gateway"),
            Map.entry("servidor-para-descubrimiento", "../servidor-para-descubrimiento")
        );
    }

    public synchronized void start(String name, int count) throws IOException {
        String dir = serviceDirs.get(name);
        if (dir == null) {
            throw new IllegalArgumentException("Servicio desconocido: " + name);
        }
        for (int i = 0; i < count; i++) {
            int port = findFreePort();
            String args = "--server.port=" + port +
                    ",--spring.boot.admin.client.instance.service-base-url=http://localhost:" + port;
            ProcessBuilder pb = new ProcessBuilder("./mvnw", "spring-boot:run",
                    "-Dspring-boot.run.arguments=" + args);
            pb.directory(new File(dir));
            pb.inheritIO();
            Process p = pb.start();
            processes.computeIfAbsent(name, k -> new ArrayList<>()).add(p);
        }
    }

    private int findFreePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        }
    }

    public synchronized void stop(String name, int count) {
        List<Process> list = processes.getOrDefault(name, Collections.emptyList());
        for (int i = 0; i < count && !list.isEmpty(); i++) {
            Process p = list.remove(list.size() - 1);
            p.destroy();
        }
    }

    public synchronized Map<String, Integer> status() {
        Map<String, Integer> status = new LinkedHashMap<>();
        for (String key : serviceDirs.keySet()) {
            status.put(key, processes.getOrDefault(key, Collections.emptyList()).size());
        }
        return status;
    }
}
