pipeline {
    agent any

    tools {
        maven 'Maven_3_9_X'
        jdk 'JDK_17'
    }

    environment {
        GITHUB_CREDENTIALS_ID = 'github-creds'
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub'
        DOCKER_IMAGE_NAME = 'tufailkarim/blog-backend-java'
    }

    stages {
        
        stage('0. Clean Workspace') {
            steps {
                // This will delete everything in the workspace
                cleanWs()
            }
        }
        
        stage('1. Checkout Code') {
            steps {
                git url: 'https://github.com/tufailkarimkhan/BE_blog.git', branch: 'master', credentialsId: 'github-creds'
            }
        }

        stage('2. Set Build Version/Tag') {
            steps {
                script {
                    env.IMAGE_TAG = "v${env.BUILD_NUMBER}"
                    echo "Image Tag: ${env.IMAGE_TAG}"
                }
            }
        }

        stage('3. Build Application JAR') {
            steps {
                script {
                    // Set environment variables for this script block
                    env.JAVA_HOME = tool 'JDK_17'
                    env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

                    // Run the build command inside the same script block
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('4. Compute JAR Name') {
            steps {
                script {
                    def artifactId = sh(
                        script: "mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout",
                        returnStdout: true
                    ).trim()
                    def version = sh(
                        script: "mvn help:evaluate -Dexpression=project.version -q -DforceStdout",
                        returnStdout: true
                    ).trim()
                    env.APP_JAR_NAME = "${artifactId}-${version}.jar"
                    echo "Computed JAR name: ${env.APP_JAR_NAME}"
                }
            }
        }

        stage('5. Build Docker Image') {
            steps {
                script {
                    def jarPath = "target/${env.APP_JAR_NAME}"
                    if (!fileExists(jarPath)) {
                        error "JAR not found: ${jarPath}"
                    }
                    docker.build(
                        "${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG}",
                        "--build-arg APP_JAR_FILE=${env.APP_JAR_NAME} ."
                    )
                }
            }
        }

        stage('6. Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub') {
                        docker.image("${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG}").push()
                        sh "docker tag ${env.DOCKER_IMAGE_NAME}:${env.IMAGE_TAG} ${env.DOCKER_IMAGE_NAME}:latest"
                        docker.image("${env.DOCKER_IMAGE_NAME}:latest").push()
                    }
                    echo 'Pushed images.'
                }
            }
        }

        stage('7. Deploy with Docker Compose') {
            steps {
                script {
                    sh 'docker-compose down || true'
                    sh 'docker-compose up -d'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo '✅ Build & Deploy Successful'
        }
        failure {
            echo '❌ Build Failed. Check logs.'
        }
    }
}
