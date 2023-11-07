./gradlew run --args=". --addPlugin=countCommits" | head -n59 | tail -n41 > countCommits.html ; (firefox countCommits.html || google-chrome countCommits.html)
