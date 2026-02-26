#!/bin/bash

echo "📦 Building app..."
mvn clean package -DskipTests

echo "🚀 Uploading jar..."
scp target/texit.jar server@192.168.0.103:/home/server/texit/texit.jar

echo "♻️ Restarting service..."
ssh server@192.168.0.103 "sudo systemctl restart access"

echo "✅ Deploy complete!"
