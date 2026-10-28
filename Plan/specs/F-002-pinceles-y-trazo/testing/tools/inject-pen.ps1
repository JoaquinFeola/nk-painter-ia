# Lápiz sintético de Windows para la QA de F-002: un trazo con la presión exacta de cada punto.
# -Points "x,y,p;x,y,p;..." en píxeles físicos de pantalla; p de 0 a 1024 (lo que entrega Windows).
# -NoPressure manda el lápiz sin máscara de presión (un lápiz que no la informa).
param(
    [Parameter(Mandatory = $true)][string]$Points,
    [int]$DelayMs = 8,
    [switch]$NoPressure
)

Add-Type -TypeDefinition @'
using System;
using System.Runtime.InteropServices;
public static class QaPen {
    [StructLayout(LayoutKind.Sequential)] public struct POINT { public int X; public int Y; }
    [StructLayout(LayoutKind.Sequential)] public struct POINTER_INFO {
        public int pointerType; public uint pointerId; public uint frameId; public uint pointerFlags;
        public IntPtr sourceDevice; public IntPtr hwndTarget;
        public POINT ptPixelLocation; public POINT ptHimetricLocation;
        public POINT ptPixelLocationRaw; public POINT ptHimetricLocationRaw;
        public uint dwTime; public uint historyCount; public int InputData; public uint dwKeyStates;
        public ulong PerformanceCount; public int ButtonChangeType;
    }
    [StructLayout(LayoutKind.Sequential)] public struct POINTER_PEN_INFO {
        public POINTER_INFO pointerInfo; public uint penFlags; public uint penMask;
        public uint pressure; public uint rotation; public int tiltX; public int tiltY;
    }
    [StructLayout(LayoutKind.Explicit, Size = 152)] public struct POINTER_TYPE_INFO {
        [FieldOffset(0)] public int type;
        [FieldOffset(8)] public POINTER_PEN_INFO penInfo;
    }
    [DllImport("user32.dll", SetLastError = true)] public static extern IntPtr CreateSyntheticPointerDevice(int type, uint maxCount, int mode);
    [DllImport("user32.dll", SetLastError = true)] public static extern bool InjectSyntheticPointerInput(IntPtr device, [In] POINTER_TYPE_INFO[] info, uint count);
    [DllImport("user32.dll")] public static extern void DestroySyntheticPointerDevice(IntPtr device);
    [DllImport("user32.dll")] public static extern bool SetProcessDPIAware();

    const int PT_PEN = 3, INRANGE = 0x2, INCONTACT = 0x4, DOWN = 0x10000, UPDATE = 0x20000, UP = 0x40000;

    static bool Send(IntPtr device, int x, int y, uint pressure, int flags, bool withPressure) {
        var info = new POINTER_TYPE_INFO[1];
        info[0].type = PT_PEN;
        info[0].penInfo.pointerInfo.pointerType = PT_PEN;
        info[0].penInfo.pointerInfo.pointerFlags = (uint)flags;
        info[0].penInfo.pointerInfo.ptPixelLocation = new POINT { X = x, Y = y };
        info[0].penInfo.penMask = withPressure ? 1u : 0u;
        info[0].penInfo.pressure = withPressure ? pressure : 0u;
        return InjectSyntheticPointerInput(device, info, 1);
    }

    public static string Stroke(int[] xs, int[] ys, uint[] ps, int delayMs, bool withPressure) {
        if (Marshal.SizeOf(typeof(POINTER_TYPE_INFO)) != 152) return "struct size " + Marshal.SizeOf(typeof(POINTER_TYPE_INFO));
        IntPtr device = CreateSyntheticPointerDevice(PT_PEN, 1, 1);
        if (device == IntPtr.Zero) return "no device, error " + Marshal.GetLastWin32Error();
        try {
            int last = xs.Length - 1;
            // A real pen hovers before it touches; Windows may drop a contact that never hovered.
            if (!Send(device, xs[0], ys[0], 0, INRANGE | UPDATE, withPressure)) return "hover refused " + Marshal.GetLastWin32Error();
            if (!Send(device, xs[0], ys[0], ps[0], INRANGE | INCONTACT | DOWN, withPressure)) return "down refused " + Marshal.GetLastWin32Error();
            for (int i = 1; i <= last; i++) {
                System.Threading.Thread.Sleep(delayMs);
                if (!Send(device, xs[i], ys[i], ps[i], INRANGE | INCONTACT | UPDATE, withPressure)) return "move refused " + Marshal.GetLastWin32Error();
            }
            System.Threading.Thread.Sleep(delayMs);
            if (!Send(device, xs[last], ys[last], 0, INRANGE | UP, withPressure)) return "up refused " + Marshal.GetLastWin32Error();
            System.Threading.Thread.Sleep(delayMs);
            Send(device, xs[last], ys[last], 0, UPDATE, withPressure);
            return "ok " + xs.Length + " points";
        } finally {
            DestroySyntheticPointerDevice(device);
        }
    }
}
'@

[QaPen]::SetProcessDPIAware() | Out-Null
$parsed = $Points.Split(';') | Where-Object { $_ } | ForEach-Object { , ($_.Split(',') | ForEach-Object { [int]$_ }) }
$xs = [int[]]($parsed | ForEach-Object { $_[0] })
$ys = [int[]]($parsed | ForEach-Object { $_[1] })
$ps = [uint32[]]($parsed | ForEach-Object { $_[2] })
[QaPen]::Stroke($xs, $ys, $ps, $DelayMs, -not $NoPressure)
