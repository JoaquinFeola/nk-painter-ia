# Círculo de US-005 E-13 en el emulador por su consola (sendevent está bloqueado sin root).
# Uso: python emu-circle.py <ruta de adb> [radio] [tandas]   · pantalla 1344×2992, centro (672, 1521)
# La consola recibe coordenadas del panel táctil (0–32767), no píxeles: se escalan.
import math, subprocess, sys, time

adb = sys.argv[1]
radius = float(sys.argv[2]) if len(sys.argv) > 2 else 480
batches = int(sys.argv[3]) if len(sys.argv) > 3 else 8
cx, cy, samples, screen_w, screen_h, axis_max = 672, 1521, 60, 1344, 2992, 32767
points = [(round((cx + radius * math.cos(2 * math.pi * i / samples)) * axis_max / screen_w),
           round((cy + radius * math.sin(2 * math.pi * i / samples)) * axis_max / screen_h)) for i in range(samples + 1)]

def send(events):
    subprocess.run([adb, 'emu', 'event', 'send'] + events, check=True, capture_output=True)

start = time.time()
x, y = points[0]
send(['EV_ABS:ABS_MT_TRACKING_ID:1', f'EV_ABS:ABS_MT_POSITION_X:{x}', f'EV_ABS:ABS_MT_POSITION_Y:{y}',
      'EV_ABS:ABS_MT_PRESSURE:1024', 'EV_KEY:BTN_TOUCH:1', 'EV_SYN:0:0'])
rest = points[1:]
size = math.ceil(len(rest) / batches)
for first in range(0, len(rest), size):
    events = []
    for x, y in rest[first:first + size]:
        events += [f'EV_ABS:ABS_MT_POSITION_X:{x}', f'EV_ABS:ABS_MT_POSITION_Y:{y}', 'EV_SYN:0:0']
    send(events)
send(['EV_ABS:ABS_MT_TRACKING_ID:-1', 'EV_KEY:BTN_TOUCH:0', 'EV_SYN:0:0'])
print('ms', round((time.time() - start) * 1000))
