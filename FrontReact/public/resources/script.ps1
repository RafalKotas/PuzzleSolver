$jsons = Get-ChildItem . *.json -rec
foreach ($file in $jsons) {
(Get-Content $file.PSPath) | ForEach-Object { $_ -replace "}", "`n}"} | Set-Content $file.PSPath
}
