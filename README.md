@"
# AiiylBankTest

Spring Boot приложение с Docker поддержкой

## Запуск
\`\`\`bash
docker build -t aiylbank .
docker run -p 8188:8080 aiylbank
\`\`\`

## API Endpoints
- Swagger UI: http://localhost:8188/swagger-ui/index.html
- H2 Console: [http://localhost:8188/h2-console](http://localhost:8188/h2-console)
"@ | Out-File -FilePath README.md -Encoding UTF8

git add README.md
git commit -m "Добавил README с инструкциями"
git push
