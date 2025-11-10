// ===== Jenkinsfile for Monolithic Application =====

pipeline {
    agent any

    environment {
        // Application info
        APP_NAME = 'devops-automation'

        // Docker configuration
        DOCKER_REGISTRY = '192.168.4.39:5000'
        DOCKER_REGISTRY_HOST = "192.168.4.39:5000"
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        SERVER_REGISTRY_CREDENTIALS = 'registry-credentials'
        DOCKER_IMAGE = "${DOCKER_REGISTRY_HOST}/${APP_NAME}".toLowerCase()

        // Deploy
        DEPLOY_USER = 'root'
        DEPLOY_SERVER = '192.168.4.39'
        SERVER_SSH_CREDENTIALS = 'server-ssh-credentials'
        REGISTRY_CREDENTIALS_PSW = 'ams123'
        REGISTRY_CREDENTIALS_USR = 'ams'

        // Maven configuration
        MAVEN_OPTS = '-Xmx2048m -Xms1024m'
        JAVA_HOME = tool name: 'JDK17', type: 'jdk'

        // Database configuration for tests
        TEST_DB_URL = 'jdbc:h2:mem:testdb'

        // Version management
        VERSION = "${env.BUILD_NUMBER}"
        GIT_COMMIT_SHORT = bat(
            script: "git rev-parse --short HEAD",
            returnStdout: true
        ).trim()
        IMAGE_TAG = Math.abs(new Random().nextInt(2000000000)).toString()
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['develop', 'staging', 'main'],
            description: 'Target deployment environment'
        )
        choice(
            name: 'DEPLOYMENT_TYPE',
            choices: ['docker-compose', 'local', 'skip'],
            description: 'Select deployment method: docker-compose, local, or skip deployment'
        )
        booleanParam(
            name: 'RUN_INTEGRATION_TESTS',
            defaultValue: true,
            description: 'Run integration tests'
        )
        booleanParam(
            name: 'RUN_PERFORMANCE_TESTS',
            defaultValue: false,
            description: 'Run performance tests'
        )
        booleanParam(
            name: 'RUN_SECURITY_SCAN',
            defaultValue: true,
            description: 'Run security vulnerability scan'
        )
    }

    triggers {
        pollSCM('*/5 * * * *')
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code from Git..."
                    checkout scm

                    bat '''
                        echo "============================================"
                        echo "Build Information"
                        echo "============================================"
                        echo "Application: ${APP_NAME}"
                        echo "Branch: ${GIT_BRANCH}"
                        echo "Commit: ${GIT_COMMIT_SHORT}"
                        echo "Build Number: ${BUILD_NUMBER}"
                        echo "Image Tag: ${IMAGE_TAG}"
                        echo "Environment: ${ENVIRONMENT}"
                        echo "============================================"
                    '''
                }
            }
        }

//         stage('Code Analysis') {
//             parallel {
//                 stage('Checkstyle') {
//                     steps {
//                         script {
//                             echo "Running Checkstyle..."
//                             bat 'mvn checkstyle:check -B'
//                         }
//                     }
//                 }

//                 stage('PMD') {
//                     steps {
//                         script {
//                             echo "Running PMD..."
//                             bat 'mvn pmd:check -B'
//                         }
//                     }
//                 }

//                 stage('SpotBugs') {
//                     steps {
//                         script {
//                             echo "Running SpotBugs..."
//                             bat 'mvn spotbugs:check -B'
//                         }
//                     }
//                 }
//             }
//         }
//
        stage('Build') {
            steps {
                script {
                    echo "Building application..."
                    bat '''
                        mvn clean compile \
                            -DskipTests=true \
                            -B -V
                    '''
                }
            }
        }

//         stage('Unit Tests') {
//             steps {
//                 script {
//                     echo "Running unit tests..."
//                     bat '''
//                         mvn test \
//                             -Dtest=*Test \
//                             -B
//                     '''
//                 }
//             }
//             post {
//                 always {
//                     junit 'target/surefire-reports/*.xml'
//
//                     publishHTML(target: [
//                         reportDir: 'target/surefire-reports',
//                         reportFiles: '*.html',
//                         reportName: 'Unit Test Report'
//                     ])
//                 }
//             }
//         }
//
//         stage('Integration Tests') {
//             when {
//                 expression { params.RUN_INTEGRATION_TESTS }
//             }
//             steps {
//                 script {
//                     echo "Starting test database..."
//
//                     sh '''
//                         # Start PostgreSQL for integration tests
//                         docker run -d \
//                             --name test-postgres \
//                             -e POSTGRES_DB=testdb \
//                             -e POSTGRES_USER=testuser \
//                             -e POSTGRES_PASSWORD=testpass \
//                             -p 5433:5432 \
//                             postgres:15-alpine
//
//                         # Wait for database to be ready
//                         sleep 10
//                     '''
//
//                     try {
//                         sh '''
//                             mvn verify \
//                                 -Dtest=*IT \
//                                 -DskipUnitTests=true \
//                                 -Dspring.datasource.url=jdbc:postgresql://localhost:5433/testdb \
//                                 -Dspring.datasource.username=testuser \
//                                 -Dspring.datasource.password=testpass \
//                                 -B
//                         '''
//                     } finally {
//                         sh 'docker stop test-postgres && docker rm test-postgres'
//                     }
//                 }
//             }
//             post {
//                 always {
//                     junit 'target/failsafe-reports/*.xml'
//                 }
//             }
//         }
//
//         stage('Quality Gate') {
//             steps {
//                 script {
//                     echo "Waiting for SonarQube Quality Gate..."
//
//                     timeout(time: 5, unit: 'MINUTES') {
//                         def qg = waitForQualityGate()
//                         if (qg.status != 'OK') {
//                             error "Quality Gate failed: ${qg.status}"
//                         }
//                         echo "✅ Quality Gate passed!"
//                     }
//                 }
//             }
//         }
//
//         stage('Security Scan') {
//             when {
//                 expression { params.RUN_SECURITY_SCAN }
//             }
//             parallel {
//                 stage('Dependency Check') {
//                     steps {
//                         script {
//                             echo "Checking for vulnerable dependencies..."
//                             sh '''
//                                 mvn dependency-check:check \
//                                     -DfailBuildOnCVSS=7 \
//                                     -B
//                             '''
//                         }
//                     }
//                     post {
//                         always {
//                             publishHTML(target: [
//                                 reportDir: 'target',
//                                 reportFiles: 'dependency-check-report.html',
//                                 reportName: 'OWASP Dependency Check Report'
//                             ])
//                         }
//                     }
//                 }
//             }
//         }
//
        stage('Package') {
            steps {
                script {
                    echo "Packaging application..."
                    bat '''
                        mvn package \
                            -DskipTests=true \
                            -B
                    '''
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }
//
//         stage('Build Docker Image') {
//             steps {
//                 script {
//                     echo "Building Docker image..."
//
//                     docker.build(
//                         "${DOCKER_IMAGE}:${IMAGE_TAG}",
//                         "--build-arg JAR_FILE=target/${APP_NAME}.jar ."
//                     )
//
//                     // Tag as latest for the environment
//                     bat "docker tag ${DOCKER_IMAGE}:${IMAGE_TAG} ${DOCKER_IMAGE}:${ENVIRONMENT}-latest"
//                 }
//             }
//         }

           stage('Build Docker Image') {
               steps {
                   script {
                       echo "Building Docker image..."

                       def gitCommit = bat(
                           script: "@echo off && for /f %%i in ('git rev-parse --short HEAD') do echo %%i",
                           returnStdout: true
                       ).trim()

                       echo "Building image tag: ${IMAGE_TAG}"

                       echo "Docker image: ${DOCKER_IMAGE}:${IMAGE_TAG}"

                       docker.build("${APP_NAME}:${IMAGE_TAG}", "--build-arg JAR_FILE=target/${APP_NAME}.jar .")

                       bat "docker images ${APP_NAME}:${IMAGE_TAG}"

                       bat """
                           docker tag ${APP_NAME}:${IMAGE_TAG} ${DOCKER_IMAGE}:${IMAGE_TAG}
                           docker tag ${APP_NAME}:${IMAGE_TAG} ${DOCKER_IMAGE}:latest
                       """

                       echo "Docker image built and tagged: ${DOCKER_IMAGE}:${IMAGE_TAG}"

                   }
               }
           }

        stage('Image Security Scan') {
            when {
                expression { params.RUN_SECURITY_SCAN }
            }
            steps {
                script {
                    echo "Scanning Docker image for vulnerabilities..."

                    bat """
                        trivy image \
                            --severity HIGH,CRITICAL \
                            --format json \
                            --output target/trivy-report.json \
                            ${DOCKER_IMAGE}:${IMAGE_TAG}
                    """

                    // Check if critical vulnerabilities found
                    def trivyReport = readJSON file: 'target/trivy-report.json'
                    def criticalCount = 0

                    if (trivyReport.Results) {
                        trivyReport.Results.each { result ->
                            if (result.Vulnerabilities) {
                                criticalCount += result.Vulnerabilities.findAll {
                                    it.Severity == 'CRITICAL'
                                }.size()
                            }
                        }
                    }

                    echo "Found ${criticalCount} critical vulnerabilities"

                    if (criticalCount > 0) {
                        error "Found ${criticalCount} critical vulnerabilities in Docker image"
                    }
                }
            }
            post {
                always {
                    publishHTML(target: [
                        reportDir: 'target',
                        reportFiles: 'trivy-report.json',
                        reportName: 'Trivy Security Report'
                    ])
                }
            }
        }
//
//         stage('Push to Registry') {
//             when {
//                 anyOf {
//                     branch 'develop'
//                     branch 'staging'
//                     branch 'main'
//                 }
//             }
//             steps {
//                 script {
//                     echo "Pushing Docker image to registry..."
//
//                     docker.withRegistry("http://${DOCKER_REGISTRY}", SERVER_REGISTRY_CREDENTIALS) {
//                         bat """
//                             docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
//                         """
//                     }
//
//                     echo "Image pushed: ${DOCKER_IMAGE}:${IMAGE_TAG}"
//                 }
//             }
//         }

        // docker push ${DOCKER_IMAGE}:${ENVIRONMENT}-latest

//         stage('Push to Registry') {
//             when {
//                 anyOf {
//                     branch 'develop'
//                     branch 'staging'
//                     branch 'main'
//                 }
//             }
//             steps {
//                 script {
//                     echo "Pushing Docker image to private registry..."
//
//                     // Define your registry info
//                     def registryHost = "192.168.4.39:5000" // your registry address
//                     def dockerImage = "${registryHost}/${DOCKER_IMAGE}"
//
//                     echo "Full image name: ${dockerImage}:${IMAGE_TAG}"
//
//                     // Tag image for private registry
//                     bat "docker tag ${DOCKER_IMAGE}:${IMAGE_TAG} ${dockerImage}:${IMAGE_TAG}"
//
//                     // Login and push using Jenkins credentials
//                     withCredentials([usernamePassword(credentialsId: 'SERVER_REGISTRY_CREDENTIALS', usernameVariable: 'REG_USER', passwordVariable: 'REG_PASS')]) {
//                         bat """
//                             docker login ${registryHost} -u %REG_USER% -p %REG_PASS%
//                             docker push ${dockerImage}:${IMAGE_TAG}
//                             docker push ${dockerImage}:latest
//                             docker logout ${registryHost}
//                         """
//                     }
//
//                     echo "✅ Image pushed successfully to ${registryHost}"
//                 }
//             }
//         }

            stage('Push to Registry') {
                steps {
                    script {
                        echo "Pushing Docker image to registry..."

                        withCredentials([usernamePassword(credentialsId: SERVER_REGISTRY_CREDENTIALS,
                                                          usernameVariable: 'REG_USER',
                                                          passwordVariable: 'REG_PASS')]) {
                            bat """
                                docker login ${env.DOCKER_IMAGE.split('/')[0]} -u %REG_USER% -p %REG_PASS%
                                docker push ${env.DOCKER_IMAGE}:${env.IMAGE_TAG}
                                docker push ${env.DOCKER_IMAGE}:latest
                                docker logout ${env.DOCKER_IMAGE.split('/')[0]}
                            """
                        }

                        echo "Image pushed: ${env.DOCKER_IMAGE}:${env.IMAGE_TAG}"
                    }
                }
            }

//             stage('Test SSH Connection') {
//                 steps {
//                     withCredentials([usernamePassword(credentialsId: SERVER_SSH_CREDENTIALS, usernameVariable: 'SSH_USER', passwordVariable: 'SSH_PASS')]) {
//                         bat '''
//                             echo "Testing SSH connection to $SSH_USER@192.168.4.39 ..."
//                             sshpass -p "$SSH_PASS" ssh -o StrictHostKeyChecking=no $SSH_USER@192.168.4.39 "echo Connected successfully && hostname && uptime"
//                         '''
//                     }
//                 }
//             }

//             stage('Deploy to Server') {
//                 steps {
//                     script {
//                         echo "🚀 Deploying to ${params.ENVIRONMENT}..."
//
//                         // Choose deployment method
//                         switch(params.DEPLOYMENT_TYPE) {
//                             case 'docker-compose':
//                                 deployWithDockerCompose()
//                                 break
//                             case 'docker-swarm':
//                                 deployWithDockerSwarm()
//                                 break
//                             case 'kubernetes':
//                                 deployToKubernetes()
//                                 break
//                             case 'manual':
//                                 deployManually()
//                                 break
//                             default:
//                                 error "Unknown deployment type: ${params.DEPLOYMENT_TYPE}"
//                         }
//                     }
//                 }
//                 }

//                 stage('Health Check') {
//                     steps {
//                         script {
//                             echo "🏥 Running health checks..."
//
//                             // Wait for application to start
//                             sleep(time: 30, unit: 'SECONDS')
//
//                             // Health check with retry
//                             def healthCheckPassed = false
//                             def maxRetries = 5
//
//                             for (int i = 0; i < maxRetries; i++) {
//                                 try {
//                                     def response = sh(
//                                         script: """
//                                             curl -f -s -o /dev/null -w "%{http_code}" \
//                                             http://${DEPLOY_SERVER}:${APP_PORT}/actuator/health
//                                         """,
//                                         returnStdout: true
//                                     ).trim()
//
//                                     if (response == '200') {
//                                         echo "✅ Health check passed!"
//                                         healthCheckPassed = true
//                                         break
//                                     }
//                                 } catch (Exception e) {
//                                     echo "Health check attempt ${i + 1} failed, retrying..."
//                                     sleep(time: 10, unit: 'SECONDS')
//                                 }
//                             }
//
//                             if (!healthCheckPassed) {
//                                 error "Health check failed after ${maxRetries} attempts"
//                             }
//                         }
//                     }
//                 }

//
//         stage('🚀 Deploy to Kubernetes') {
//             when {
//                 allOf {
//                     anyOf {
//                         branch 'main'
//                         branch 'develop'
//                     }
//                     expression { params.DEPLOY_TO_K8S }
//                 }
//             }
//             steps {
//                 script {
//                     echo "Deploying to Kubernetes ${params.ENVIRONMENT} environment..."
//
//                     withKubeConfig([credentialsId: K8S_CREDENTIALS_ID]) {
//                         // Update deployment with new image
//                         sh """
//                             kubectl set image deployment/${APP_NAME} \
//                                 ${APP_NAME}=${DOCKER_IMAGE}:${IMAGE_TAG} \
//                                 -n ${K8S_NAMESPACE}
//
//                             # Verify rollout
//                             kubectl rollout status deployment/${APP_NAME} \
//                                 -n ${K8S_NAMESPACE} \
//                                 --timeout=5m
//                         """
//
//                         // Get deployment info
//                         sh """
//                             echo "Deployment status:"
//                             kubectl get deployment ${APP_NAME} -n ${K8S_NAMESPACE}
//
//                             echo "\\nPods:"
//                             kubectl get pods -l app=${APP_NAME} -n ${K8S_NAMESPACE}
//
//                             echo "\\nServices:"
//                             kubectl get svc ${APP_NAME} -n ${K8S_NAMESPACE}
//                         """
//                     }
//                 }
//             }
//         }
//
//         stage('💨 Smoke Tests') {
//             when {
//                 allOf {
//                     anyOf {
//                         branch 'main'
//                         branch 'develop'
//                     }
//                     expression { params.DEPLOY_TO_K8S }
//                 }
//             }
//             steps {
//                 script {
//                     echo "Running smoke tests..."
//
//                     // Wait for pods to be ready
//                     sleep(time: 30, unit: 'SECONDS')
//
//                     sh '''
//                         # Get service URL
//                         SERVICE_URL=$(kubectl get svc ${APP_NAME} -n ${K8S_NAMESPACE} -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
//
//                         if [ -z "$SERVICE_URL" ]; then
//                             SERVICE_URL="localhost:8080"
//                         fi
//
//                         # Test health endpoint
//                         echo "Testing health endpoint..."
//                         curl -f http://${SERVICE_URL}/actuator/health || exit 1
//
//                         # Test metrics endpoint
//                         echo "Testing metrics endpoint..."
//                         curl -f http://${SERVICE_URL}/actuator/metrics || exit 1
//
//                         # Test API docs
//                         echo "Testing API docs..."
//                         curl -f http://${SERVICE_URL}/swagger-ui.html || exit 1
//
//                         echo "✅ All smoke tests passed!"
//                     '''
//                 }
//             }
//         }
//
//         stage('⚡ Performance Tests') {
//             when {
//                 expression { params.RUN_PERFORMANCE_TESTS }
//             }
//             steps {
//                 script {
//                     echo "Running performance tests with JMeter..."
//
//                     sh '''
//                         # Run JMeter tests
//                         jmeter -n \
//                             -t tests/performance/load-test.jmx \
//                             -l target/jmeter-results.jtl \
//                             -e -o target/jmeter-report \
//                             -Jthreads=50 \
//                             -Jrampup=10 \
//                             -Jduration=300
//                     '''
//                 }
//             }
//             post {
//                 always {
//                     perfReport(
//                         sourceDataFiles: 'target/jmeter-results.jtl',
//                         errorFailedThreshold: 5,
//                         errorUnstableThreshold: 2,
//                         relativeFailedThresholdPositive: 20,
//                         relativeUnstableThresholdPositive: 10
//                     )
//
//                     publishHTML(target: [
//                         reportDir: 'target/jmeter-report',
//                         reportFiles: 'index.html',
//                         reportName: 'JMeter Performance Report'
//                     ])
//                 }
//             }
//         }
//
//         stage('📝 Generate Documentation') {
//             steps {
//                 script {
//                     echo "Generating project documentation..."
//
//                     sh '''
//                         # Generate JavaDoc
//                         mvn javadoc:javadoc
//
//                         # Generate site
//                         mvn site:site
//                     '''
//                 }
//             }
//             post {
//                 always {
//                     publishHTML(target: [
//                         reportDir: 'target/site',
//                         reportFiles: 'index.html',
//                         reportName: 'Project Documentation'
//                     ])
//                 }
//             }
//         }
    }
//
//     post {
//         always {
//             script {
//                 echo "Pipeline execution completed"
//
//                 // Clean workspace
//                 cleanWs()
//             }
//         }
//     }
}

// ===== 2. Deployment Functions =====

def deployWithDockerCompose() {
    echo "Deploying using Docker Compose with username/password..."

    withCredentials([usernamePassword(credentialsId: 'server-ssh-credentials', usernameVariable: 'SSH_USER', passwordVariable: 'SSH_PASS')]) {
        bat '''
            echo =====================================================
            echo Connecting to %DEPLOY_SERVER% as %SSH_USER% ...
            echo =====================================================

            plink -ssh %SSH_USER%@%DEPLOY_SERVER% -pw %SSH_PASS% ^
                "cd /opt/ci-cd-pipeline && \
                 echo ===================================================== && \
                 echo Logging into private Docker registry... && \
                 docker login %DOCKER_REGISTRY% -u %REGISTRY_CREDENTIALS_USR% -p %REGISTRY_CREDENTIALS_PSW% && \
                 echo ===================================================== && \
                 echo Pulling latest image version... && \
                 docker-compose pull && \
                 echo ===================================================== && \
                 echo Stopping and removing old containers... && \
                 docker-compose down && \
                 echo ===================================================== && \
                 echo Starting new containers... && \
                 docker-compose up -d && \
                 echo ===================================================== && \
                 echo Verifying container status... && \
                 docker-compose ps && \
                 echo ===================================================== && \
                 echo Deployment completed successfully!"
        '''
    }
}

// def deployWithDockerCompose() {
//     echo "🚀 Deploying using Docker Compose with username/password..."
//
//     withCredentials([usernamePassword(credentialsId: 'server-ssh-credentials', usernameVariable: 'SSH_USER', passwordVariable: 'SSH_PASS')]) {
//         // Use PowerShell-compatible command
//         bat """
//             # echo open ${DEPLOY_SERVER} > sftp.txt
//             # echo ${SSH_USER} >> sftp.txt
//             # echo ${SSH_PASS} >> sftp.txt
//
//             echo ==== Connecting to ${DEPLOY_SERVER} ====
//             plink -ssh %SSH_USER%@${DEPLOY_SERVER} -pw %SSH_PASS% ^
//                 "cd /opt/ci-cd-pipeline && ^
//
//                  echo ===================================================== && ^
//                  echo 🔐 Logging into private Docker registry... && ^
//                  docker login ${DOCKER_REGISTRY} -u %SSH_USER% -p %SSH_PASS% && ^
//
//                  echo ===================================================== && ^
//                  echo 🔄 Pulling latest image version... && ^
//                  docker-compose pull && ^
//
//                  echo ===================================================== && ^
//                  echo 🧹 Stopping and removing old containers... && ^
//                  docker-compose down && ^
//
//                  echo ===================================================== && ^
//                  echo 🚀 Starting new containers... && ^
//                  docker-compose up -d && ^
//
//                  echo ===================================================== && ^
//                  echo 📋 Verifying container status... && ^
//                  docker-compose ps && ^
//
//                  echo ===================================================== && ^
//                  echo ✅ Deployment completed"
//         """
//     }
// }


// def deployWithDockerCompose() {
//     echo "Deploying using Docker Compose..."
//
//     sshagent(credentials: ['server-ssh-credentials']) {
//         bat """
//             ssh ${DEPLOY_USER}@${DEPLOY_SERVER} "
//                 cd /opt/ci-cd-pipeline
//
//                 # Update .env file with new image tag
//                 # echo 'IMAGE_TAG=${IMAGE_TAG}' > .env
//                 # echo 'REGISTRY=${PRIVATE_REGISTRY}' >> .env
//
//                 # Login to registry
//                 echo "Login to registry"
//                 docker login ${PRIVATE_REGISTRY} -u ${REGISTRY_CREDENTIALS_USR} -p ${REGISTRY_CREDENTIALS_PSW}
//
//                 # Pull and restart
//                 docker-compose pull
//                 docker-compose up -d
//
//                 # Check status
//                 docker-compose ps
//
//                 echo '✅ Docker Compose deployment completed!'
//             "
//         """
//     }
// }

// def deployWithDockerCompose() {
//     echo "Deploying with Docker Compose..."

//    withCredentials([sshUserPrivateKey(credentialsId: 'SERVER_SSH_CREDENTIALS', keyFileVariable: 'SSH_KEY')]) {
//            sh """
//            ssh -i $SSH_KEY -o StrictHostKeyChecking=no root@192.168.4.39 '
//                cd /opt/devops-automation &&
//                sed -i "s|image: 192.168.4.39:5000/devops-automation:.*|image: 192.168.4.39:5000/devops-automation:${imageTag}|" docker-compose.yml &&
//                docker-compose pull &&
//                docker-compose down &&
//                docker-compose up -d
//            '
//            """
//        }

//     sshagent(credentials: [SERVER_SSH_CREDENTIALS]) {
//     withCredentials([usernamePassword(credentialsId: SERVER_SSH_CREDENTIALS, usernameVariable: 'SSH_USER', passwordVariable: 'SSH_PASS')]) {
//         bat """
//             ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${DEPLOY_SERVER}
//                 cd /opt/ci-cd-pipeline
//
//                 # Update docker-compose.yml with new image
//                 export IMAGE_TAG=${IMAGE_TAG}
//
//                 # Pull new image
//                 echo "🔄 Pulling latest image..."
//                 docker-compose pull
//
//                 # Stop old containers
//                 echo "🧹 Stopping old containers..."
//                 docker-compose down
//
//                 # Start new containers
//                 echo "🚀 Starting new containers..."
//                 docker-compose up -d
//
//                 # Verify containers are running
//                 docker-compose ps
//
//                 echo "✅ Deployment completed!"
//         """
//     }
//     withCredentials([usernamePassword(credentialsId: SERVER_SSH_CREDENTIALS, usernameVariable: 'SSH_USER', passwordVariable: 'SSH_PASS')]) {
//         powershell '''
//             $server = "192.168.4.39"
//             $username = $env:SSH_USER
//             $password = $env:SSH_PASS
//             $imageTag = $env:IMAGE_TAG
//
//             Write-Host "🔗 Connecting to $server as $username ..."
//
//             # Verify plink is available
//             if (-not (Get-Command plink.exe -ErrorAction SilentlyContinue)) {
//                 Write-Error "❌ plink.exe not found. Please install PuTTY and add it to PATH."
//                 exit 1
//             }
//
//             $remoteCommand = @"
//     cd /opt/ci-cd-pipeline
//     echo '🔄 Pulling latest image...'
//     docker-compose pull
//     echo '🧹 Stopping old containers...'
//     docker-compose down
//     echo '🚀 Starting new containers...'
//     docker-compose up -d
//     docker-compose ps
//     echo '✅ Deployment completed!'
//     "@
//
//             # Execute remotely
//             plink.exe -ssh $username@$server -pw $password "$remoteCommand"
//
//             if ($LASTEXITCODE -ne 0) {
//                 Write-Error "❌ Remote deployment failed!"
//                 exit 1
//             }
//         '''
//     }
//
// }
//
// def deployWithDockerSwarm() {
//     echo "Deploying with Docker Swarm..."
//
//     sshagent(credentials: ['deploy-ssh-key']) {
//         bat """
//             ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${DEPLOY_SERVER} << 'EOF'
//                 # Update service
//                 docker service update \
//                     --image ${DOCKER_IMAGE}:${IMAGE_TAG} \
//                     --update-parallelism 1 \
//                     --update-delay 10s \
//                     ${CONTAINER_NAME}
//
//                 # Wait for rollout
//                 docker service ps ${CONTAINER_NAME}
//
//                 echo "✅ Swarm deployment completed!"
// EOF
//         """
//     }
// }
//
// def deployToKubernetes() {
//     echo "Deploying to Kubernetes..."
//
//     withKubeConfig([credentialsId: 'kubeconfig-credentials']) {
//         bat """
//             # Update deployment image
//             kubectl set image deployment/${K8S_DEPLOYMENT} \
//                 ${K8S_DEPLOYMENT}=${DOCKER_IMAGE}:${IMAGE_TAG} \
//                 -n ${K8S_NAMESPACE}
//
//             # Wait for rollout
//             kubectl rollout status deployment/${K8S_DEPLOYMENT} \
//                 -n ${K8S_NAMESPACE} \
//                 --timeout=5m
//
//             # Verify pods
//             kubectl get pods -n ${K8S_NAMESPACE} -l app=${K8S_DEPLOYMENT}
//
//             echo "✅ Kubernetes deployment completed!"
//         """
//     }
// }
//
// def deployManually() {
//     echo "Deploying manually with Docker commands..."
//
//     sshagent(credentials: ['deploy-ssh-key']) {
//         bat """
//             ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${DEPLOY_SERVER} << 'EOF'
//                 # Pull new image
//                 docker pull ${DOCKER_IMAGE}:${IMAGE_TAG}
//
//                 # Stop old container
//                 docker stop ${CONTAINER_NAME} 2>/dev/null || true
//                 docker rm ${CONTAINER_NAME} 2>/dev/null || true
//
//                 # Start new container
//                 docker run -d \
//                     --name ${CONTAINER_NAME} \
//                     --restart unless-stopped \
//                     -p ${APP_PORT}:8080 \
//                     -e SPRING_PROFILES_ACTIVE=${params.ENVIRONMENT} \
//                     -e DATABASE_URL=\${DATABASE_URL} \
//                     -e DATABASE_USERNAME=\${DATABASE_USERNAME} \
//                     -e DATABASE_PASSWORD=\${DATABASE_PASSWORD} \
//                     ${DOCKER_IMAGE}:${IMAGE_TAG}
//
//                 # Verify container is running
//                 docker ps | grep ${CONTAINER_NAME}
//
//                 echo "✅ Manual deployment completed!"
// EOF
//         """
//     }
// }
//
// def rollbackDeployment(previousImage) {
//     echo "Rolling back to previous image: ${previousImage}..."
//
//     sshagent(credentials: ['deploy-ssh-key']) {
//         bat """
//             ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${DEPLOY_SERVER} << 'EOF'
//                 # Stop current container
//                 docker stop ${CONTAINER_NAME}
//                 docker rm ${CONTAINER_NAME}
//
//                 # Start previous version
//                 docker run -d \
//                     --name ${CONTAINER_NAME} \
//                     --restart unless-stopped \
//                     -p ${APP_PORT}:8080 \
//                     ${previousImage}
//
//                 echo "✅ Rollback completed!"
// EOF
//         """
//     }
// }
