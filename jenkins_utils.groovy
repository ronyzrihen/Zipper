def runZipJob() {
    echo 'Running zip_job.py to generate zip files...'
    try {
        sh 'python3 ./job/zip_job.py'
    } catch (IOException e) {
        error "Error running zip_job.py: ${e.getMessage()}"
    }
    def zipFiles = sh(script: 'ls zipped/*.zip 2>/dev/null || true', returnStdout: true).trim()
    if (zipFiles == '') {
        error "No zip files found in 'zipped/' directory to upload."
    }
}

def uploadToArtifactory() {
    echo 'Uploading artifacts to Artifactory...'
    server = Artifactory.server 'zip-artifacts'
    try {
        if (server == null) {
            error "Artifactory server instance is missing!"
        }
        buildInfo = Artifactory.newBuildInfo()
        buildInfo.env.capture = true
        uploadSpec = """{
            "files": [
                {
                    "pattern": "zipped/*.zip",
                    "target": "${env.ARTIFACTORY_REPO}${env.VERSION}/"
                }
            ]
        }"""

        server.upload(uploadSpec, buildInfo)
        server.publishBuildInfo(buildInfo)
    } catch (IOException e) {
        error "Error uploading artifacts to Artifactory: ${e.getMessage()}"
    }
    echo "Artifacts uploaded successfully to ${env.ARTIFACTORY_REPO}${env.VERSION}/"
}

def sendEmailReport(String recipient = null) {
    def buildStatus = currentBuild.currentResult
    def subject = "Build #${env.BUILD_NUMBER} - ${buildStatus}"
    def body = """
        Build Report:\n
        Project: ${env.JOB_NAME}\n
        Status: ${buildStatus}\n
        Version: ${env.VERSION}\n
    """

    emailext(
        to: recipient ?: "$DEFAULT_RECIPIENTS",
        subject: subject,
        body: body,
        mimeType: 'text/plain',
    )
    echo "Email report sent to ${recipient}"
}

def cleanWorkspace() {
    echo 'Cleaning up workspace...'
    deleteDir()
}

return this
