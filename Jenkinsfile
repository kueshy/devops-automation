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
