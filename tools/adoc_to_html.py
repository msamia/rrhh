import os
import re

INPUT_DIR = 'servicio-openapi-ui/src/main/resources/static/adoc'
OUTPUT_DIR = 'servicio-openapi-ui/src/main/resources/static/html'

os.makedirs(OUTPUT_DIR, exist_ok=True)

for fname in os.listdir(INPUT_DIR):
    if not fname.endswith('.adoc'):
        continue
    in_path = os.path.join(INPUT_DIR, fname)
    out_path = os.path.join(OUTPUT_DIR, fname.replace('.adoc', '.html'))
    html_lines = ['<!DOCTYPE html>', '<html>', '<body>']
    in_ul = []
    with open(in_path, 'r', encoding='utf-8') as f:
        for line in f:
            line = line.rstrip('\n')
            line = re.sub(r'`([^`]+)`', r'<code>\1</code>', line)
            if line.startswith('= '):
                html_lines.append(f'<h1>{line[2:].strip()}</h1>')
            elif line.startswith('== '):
                html_lines.append(f'<h2>{line[3:].strip()}</h2>')
            elif line.startswith('** '):
                if len(in_ul) < 2:
                    while len(in_ul) < 2:
                        html_lines.append('<ul>')
                        in_ul.append(True)
                html_lines.append(f'<li>{line[3:].strip()}</li>')
            elif line.startswith('* '):
                if not in_ul:
                    html_lines.append('<ul>')
                    in_ul.append(True)
                elif len(in_ul) > 1:
                    while len(in_ul) > 1:
                        html_lines.append('</ul>')
                        in_ul.pop()
                html_lines.append(f'<li>{line[2:].strip()}</li>')
            elif not line.strip():
                while in_ul:
                    html_lines.append('</ul>')
                    in_ul.pop()
                html_lines.append('<br>')
            else:
                html_lines.append(f'<p>{line.strip()}</p>')
    while in_ul:
        html_lines.append('</ul>')
        in_ul.pop()
    html_lines.extend(['</body>', '</html>'])
    with open(out_path, 'w', encoding='utf-8') as out:
        out.write('\n'.join(html_lines))
