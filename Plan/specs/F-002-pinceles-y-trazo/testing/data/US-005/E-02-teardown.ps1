# US-005 E-02 (Windows): devuelve las preferencias guardadas por el setup; borra solo las que creó la corrida.
$file = Join-Path $env:APPDATA 'nk-painter\drawing-settings.txt'
$backup = "$file.qa-bak"
if (Test-Path $backup) {
    if (Test-Path $file) { Remove-Item $file }
    Move-Item $backup $file
    'preferencias devueltas'
} else {
    'no había preferencias antes de la corrida; quedan las que dejó la app'
}
