def runZipJob() {
    echo "Running zip_job.py to generate zip files..."
    sh 'python3 ./job/zip_job.py'
    // TODO: add error handling in case script fails
}

def uploadToArtifactory(String serverId) {
    echo "Uploading artifacts to Artifactory..."
    def server = Artifactory.server(serverId)
    server.credentialsId = env.ARTIFACTORY_CREDS
    
    def buildInfo = Artifactory.newBuildInfo()
    buildInfo.env.capture = true

    // TODO: add error handling in case upload fails
    def uploadSpec = """{ 
        "files": [
            {
                "pattern": "zipped/*.zip",
                "target": "${env.ARTIFACTORY_REPO}${env.VERSION}/"
            }
        ]
    }"""

    server.upload(uploadSpec, buildInfo)
    server.publishBuildInfo(buildInfo)
    echo "Artifacts uploaded successfully to ${env.ARTIFACTORY_REPO}${env.VERSION}/"
}


def sendEmailReport(String recipient) {
    def buildStatus = currentBuild.currentResult
    def subject = "Build #${env.BUILD_NUMBER} - ${buildStatus}"
    def body = """
        Build Report:\n
        Project: ${env.JOB_NAME}\n
        Status: ${buildStatus}\n
        Version: ${env.VERSION}\n
    """

    emailext(
        to: recipient,
        subject: subject,
        body: body,
        mimeType: 'text/plain'
    )
    echo "Email report sent to ${recipient}"
}

def cleanWorkspace() {
    echo "Cleaning up workspace..."
    deleteDir()
}


return this
