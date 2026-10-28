# US-005 E-02 (Windows): deja la app sin preferencias de dibujo, guardando las que hubiera.
$file = Join-Path $env:APPDATA 'nk-painter\drawing-settings.txt'
$backup = "$file.qa-bak"
if (Test-Path $backup) { throw "Ya hay un $backup de una corrida anterior: correr el teardown primero." }
if (Test-Path $file) { Move-Item $file $backup }
if (Test-Path $file) { throw 'Las preferencias siguen ahí.' }
'sin preferencias de dibujo'
