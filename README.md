# VWO Tech Debt Client
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

This open source library allows you to send your feature flag usage to VWO to help you reduce your tech debt.

## Requirements
The tech debt client runs as a JAR file, and you can run it either on your local dev environment, or as a Jenkins Job.
You can either use the JAR included in the repo, or you can build it from the source.

## Jenkins Job
You can run the client from a Jenkins job

```jenkins
java -jar <location_of_tech_debt_client> sourceFolder="${WORKSPACE}" accountId=${accountId} isReferenceCode=${isReferenceCode} APIToken=${apiToken} repoBranch="${GIT_BRANCH}" repoURL="${GIT_URL}"
```

Variables :

location_of_tech_debt_client - /opt/automata/vwo-techdebt-client.jar (as an example)

GIT_BRANCH, GIT_URL, WORKSPACE - variables available in Jenkins

accountId - VWO accountId

isReferenceCode - set to true if you want to send your code snippets to VWO

APIToken - VWO Developer API Token


## Third-party Resources and Credits

Refer [third-party-attributions.txt](https://github.com/wingify/vwo-tech-debt-client/blob/master/third-party-attributions.txt)

## Authors

* Core Contributor & Maintainer - [rohitesh-wingify](https://github.com/rohitesh-wingify)

## Changelog

Refer [CHANGELOG.md](https://github.com/wingify/vwo-tech-debt-client/blob/master/CHANGELOG.md)

## Contributing

Please go through our [contributing guidelines](https://github.com/wingify/vwo-tech-debt-client/blob/master/CONTRIBUTING.md)
## Code of Conduct

[Code of Conduct](https://github.com/wingify/vwo-tech-debt-client/blob/master/CODE_OF_CONDUCT.md)

## License

[Apache License, Version 2.0](https://github.com/wingify/vwo-tech-debt-client/blob/master/LICENSE)

Copyright 2024 Wingify Software Pvt. Ltd.
