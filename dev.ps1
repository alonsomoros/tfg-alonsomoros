param()

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$mvnw = Join-Path $root 'mvnw.cmd'

$services = @(
    @{ Name = 'subscription-service'; Pom = 'subscription-service\pom.xml' },
    @{ Name = 'recurring-engine'; Pom = 'recurring-engine\pom.xml' }
)

foreach ($service in $services) {
    $pomPath = Join-Path $root $service.Pom
    
    $scriptBlock = @"
        `$host.UI.RawUI.WindowTitle = '$($service.Name)';
        Set-Location '$root';
        Write-Host 'Iniciando $($service.Name)...' -ForegroundColor Cyan;
        & '$mvnw' -f '$pomPath' spring-boot:run
"@

    Start-Process -FilePath 'powershell.exe' `
        -ArgumentList @('-NoExit', '-Command', $scriptBlock) `
        -WorkingDirectory $root `
        -WindowStyle Maximized | Out-Null
}

Write-Host 'The two services have been launched in separate windows.' -ForegroundColor Green