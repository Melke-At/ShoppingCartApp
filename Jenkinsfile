pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    environment {
        PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
        DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
        DOCKERHUB_REPO = 'melkamuy/shoppingcartapp'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {

        // 1️⃣ Checkout the repository
        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/Melke-At/ShoppingCartApp.git'
            }
        }

        // 2️⃣ Run unit tests
        stage('Run Tests') {
            steps {
                bat 'mvn clean test'
            }
        }

        // 3️⃣ Publish test results
        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        // 4️⃣ Package the application into a JAR
        stage('Package') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        // 5️⃣ Build Docker image
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                }
            }
        }

        // 6️⃣ Push Docker image to Docker Hub
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                        docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }
    }
}