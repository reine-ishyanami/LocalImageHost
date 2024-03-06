Get-ChildItem -Filter "target/*.jar" -File | ForEach-Object {
  $newname = $_.Name -replace ".jar", "-windows.jar"
  Rename-Item $_.FullName $newname
}
