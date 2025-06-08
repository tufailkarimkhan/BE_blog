// Jenkinsfile (in your Spring Boot app’s Git repo root)
pipeline {
    agent any  // Runs on any available Jenkins agent/node

    tools {
        maven 'Maven_3_9_X' // Matches name in Jenkins’s Global Tool Configuration
        jdk   'JDK_17'      // Matches name in Jenkins’s Global Tool Configuration
    }

    environment {
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub'                        // ID of Docker Hub creds in Jenkins
        DOCKER_IMAGE_NAME        = 'tufailkarim/blog-backend-java'     // Your Docker Hub username/image_name

        // ▼ Remove this static entry:
        // APP_JAR_NAME = 'springboot-blog-rest-api-0.0.1-SNAPSHOT.jar'
        //
        // We’ll compute APP_JAR_NAME dynamically in a dedicated stage instead.
    }

    stages {
        stage('1. Checkout Code') {
            steps {
                git branch: 'master',
                    url:           'https://github.com/tufailkarimkhan/BE_blog.git',

            }
        }

        stage('2. Set Build Version/Tag') {
            steps {
                script {
                    // Use Jenkins build number for a unique image tag, e.g. "v5"
                    env.IMAGE_TAG = "v${env.BUILD_NUMBER}"
                    echo "Building with Image Tag: ${env.IMAGE_TAG}"
                }
            }
        }

        stage('3. Build Application JAR') {
            steps {
                script {
                    // Make sure Jenkins uses the correct JDK
                    env.JAVA_HOME = tool 'JDK_17'
                    env.PATH      = "${env.JAVA_HOME}/bin:${env.PATH}"
                }
                sh './mvnw clean package -DskipTests'
            }
        }

        // ══════════════════════════════════════════════════════════════════════
        // ▼ INSERT THIS NEW STAGE HERE to compute APP_JAR_NAME dynamically:
        stage('3.1 Compute JAR Name') {
            steps {
                script {
                    // 1) Grab artifactId from pom.xml
                    def artifactId = sh(
                        script: "mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout",
                        returnStdout: true
                    ).trim()

                    // 2) Grab version from pom.xml
                    def version = sh(
                        script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                        returnStdout: true
                    ).trim()

                    // 3) Construct "<artifactId>-<version>.jar" and store in env.APP_JAR_NAME
                    env.APP_JAR_NAME = "${artifactId}-${version}.jar"
                    echo "→ Computed JAR file name: ${env.APP_JAR_NAME}"
                }
            }
        }
        // ══════════════════════════════════════════════════════════════════════

        stage('4. Build Docker Image') {
            // This stage assumes your Dockerfile uses:
            //   ARG APP_JAR_FILE
            //   COPY target/${APP_JAR_FILE} /app/app.jar
            // (We pass APP_JAR_FILE below.)
            steps {
                script {
                    // 1) Verify that the computed JAR actually exists
                    def jarPath = "target/${env.APP_JAR_NAME}"
                    if (!fileExists(jarPath)) {
                        error "JAR file not found at ${jarPath}. Check Maven build output and APP_JAR_NAME."
                    }

                    // 2) Build the Docker image, passing the JAR name as a build-arg
                    docker.build(
                        "${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG}",
                        "--build-arg APP_JAR_FILE=${env.APP_JAR_NAME} ."
                    )
                }
            }
        }

        stage('5. Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', env.DOCKERHUB_CREDENTIALS_ID) {
                        sh "docker push ${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG}"
                        // Optionally:
                        // sh "docker tag ${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG} ${env.DOCKER_IMAGE_NAME}:latest"
                        // sh "docker push ${env.DOCKER_IMAGE_NAME}:latest"
                    }
                    echo "Docker Image pushed: ${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG}"
                }
            }
        }

        stage('6. Deploy Application Containers') {
            steps {
                script {
                    echo "Starting backend and DB containers using docker-compose"
                    sh 'docker-compose down' // Optional: stop old containers
                    sh 'docker-compose up -d'
                }
            }
        }

        // …(Optional) other stages …
    }

    post {
        always {
            echo 'Pipeline finished.'
            cleanWs() // Clean workspace after build
        }
        success {
            echo "Pipeline successful! Image ${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG} is ready."
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
