// pipeline {
//     environment{
//         DOCKERHUB_CREDENTIALS = credentials('dockerhub')
//     }
//     agent any
//
//     tools {
//         // Install the Maven version configured as "M3" and add it to the path.
//         maven "m3"
//     }
//
//     stages{
//         stage("Build maven"){
//             steps{
//                 checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: '382aff30-5c32-4dbc-acfd-af531b831dd9', url: 'https://github.com/kueshy/devops-automation']])
//                 bat "mvn clean install"
//             }
//         }
//         stage("Build docker image"){
//             steps{
//                 script{
//                     bat "docker build -t codedev001/devops-integration ."
//                 }
//             }
//         }
//         stage("Push docker image to docker hub"){
//             steps{
//                 script{
//                     withCredentials([string(credentialsId: 'dockerhub', variable: 'dockerhubpwd')]) {
//                     bat "docker login -u codedev001 -p ${dockerhubpwd}"
//                     bat "docker push codedev001/devops-integration"
//                     }
//                 }
//             }
//         }
//     }
// }

// pipeline{
//     agent any
//     tools {
//         maven 'maven_3_9_11'
//     }
//     stages {
//         stage('Build Maven'){
//             steps{
//                 checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/kueshy/devops-automation']])
//                 bat 'mvn clean install'
//             }
//         }
//         stage('Build Docker Image'){
//             steps{
//                 script {
//                     bat 'docker build -t codedev001/devops-integration .'
//                 }
//             }
//         }
//         stage('Push Docker Image to Docker Hub'){
//             steps{
//                 script {
//                     withCredentials([string(credentialsId: 'dockerhub_pwd', variable: 'dockerhub_pwd')]) {
//                         bat "docker login -u codedev001 -p ${dockerhub_pwd}"
//                         bat "docker push codedev001/devops-integration"
//                     }
//                 }
//             }
//         }
//     }
// }

// ===== Jenkinsfile for Monolithic Application =====

pipeline {
    agent any

    environment {
        // Application info
        APP_NAME = 'devops-automation'

        // Docker configuration
        DOCKER_REGISTRY = 'docker.io'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
        DOCKER_IMAGE = "${DOCKER_REGISTRY}/${APP_NAME}"

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
        IMAGE_TAG = "${VERSION}-${GIT_COMMIT_SHORT}"
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['dev', 'staging', 'main'],
            description: 'Target deployment environment'
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
        pollSCM('H/5 * * * *')
        githubPush()
    }

//     options {
//         buildDiscarder(logRotator(numToKeepStr: '10'))
//         timeout(time: 1, unit: 'HOURS')
//         disableConcurrentBuilds()
//         timestamps()
//         ansiColor('xterm')
//     }

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
        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."

                    docker.build(
                        "${DOCKER_IMAGE}:${IMAGE_TAG}",
                        "--build-arg JAR_FILE=target/${APP_NAME}.jar ."
                    )

                    // Tag as latest for the environment
                    bat "docker tag ${DOCKER_IMAGE}:${IMAGE_TAG} ${DOCKER_IMAGE}:${ENVIRONMENT}-latest"
                }
            }
        }
//
//         stage('Image Security Scan') {
//             when {
//                 expression { params.RUN_SECURITY_SCAN }
//             }
//             steps {
//                 script {
//                     echo "Scanning Docker image for vulnerabilities..."
//
//                     sh """
//                         trivy image \
//                             --severity HIGH,CRITICAL \
//                             --format json \
//                             --output target/trivy-report.json \
//                             ${DOCKER_IMAGE}:${IMAGE_TAG}
//                     """
//
//                     // Check if critical vulnerabilities found
//                     def trivyReport = readJSON file: 'target/trivy-report.json'
//                     def criticalCount = 0
//
//                     if (trivyReport.Results) {
//                         trivyReport.Results.each { result ->
//                             if (result.Vulnerabilities) {
//                                 criticalCount += result.Vulnerabilities.findAll {
//                                     it.Severity == 'CRITICAL'
//                                 }.size()
//                             }
//                         }
//                     }
//
//                     echo "Found ${criticalCount} critical vulnerabilities"
//
//                     if (criticalCount > 0) {
//                         error "Found ${criticalCount} critical vulnerabilities in Docker image"
//                     }
//                 }
//             }
//             post {
//                 always {
//                     publishHTML(target: [
//                         reportDir: 'target',
//                         reportFiles: 'trivy-report.json',
//                         reportName: 'Trivy Security Report'
//                     ])
//                 }
//             }
//         }
//
//         stage('Push to Registry') {
//             when {
//                 anyOf {
//                     branch 'main'
//                     branch 'develop'
//                 }
//             }
//             steps {
//                 script {
//                     echo "Pushing Docker image to registry..."
//
//                     docker.withRegistry("https://${DOCKER_REGISTRY}", DOCKER_CREDENTIALS_ID) {
//                         sh """
//                             docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
//                             docker push ${DOCKER_IMAGE}:${ENVIRONMENT}-latest
//                         """
//                     }
//
//                     echo "Image pushed: ${DOCKER_IMAGE}:${IMAGE_TAG}"
//                 }
//             }
//         }
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
