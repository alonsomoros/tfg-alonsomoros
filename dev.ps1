param()

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$mvnw = Join-Path $root 'mvnw.cmd'

$services = @(
    @{ Name = 'subscription-service'; Pom = 'subscription-service\pom.xml' },
    @{ Name = 'recurring-engine'; Pom = 'recurring-engine\pom.xml' },
    @{ Name = 'payment-gateway-api'; Pom = 'payment-gateway-api\pom.xml' }
)

foreach ($service in $services) {
    $pomPath = Join-Path $root $service.Pom
    $command = "Set-Location '$root'; & '$mvnw' -f '$pomPath' spring-boot:run"

    Start-Process -FilePath 'powershell.exe' `
        -ArgumentList @('-NoExit', '-Command', $command) `
        -WorkingDirectory $root `
        -WindowStyle Maximized | Out-Null
}

Write-Host 'Los 3 servicios se han lanzado en ventanas separadas.'
