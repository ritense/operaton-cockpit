# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

#### Operaton references

* https://docs.operaton.org/docs/documentation/webapps/shared-options/authentication
* https://docs.operaton.org/docs/documentation/user-guide/security
* https://github.com/operaton/operaton-keycloak

#### Camunda references

* https://forum.camunda.io/t/single-sign-on-implementation-in-camunda/22868/3
* https://docs.camunda.io/docs/components/best-practices/operations/securing-camunda-c7/#securing-camunda-with-authentication-and-authorizations
* https://github.com/camunda-community-hub/camunda-platform-7-keycloak



### Releasing

Releases are published manually: GitHub → Actions → "Build and Push Docker Image" → Run workflow.

* `version`: plain `X.Y.Z` (MAJOR for breaking changes, MINOR for features, PATCH for fixes).
* `snapshot` (on by default): publishes `X.Y.Z-SNAPSHOT` from any branch. Re-publishing the same snapshot overwrites it. No git tag or GitHub Release is created.
* `snapshot` off: a real release. Only allowed from `main`. Pushes image tags `X.Y.Z`, `X.Y` and `latest`, then creates git tag `vX.Y.Z` and a GitHub Release.

The version must be strictly greater than the latest `v*` tag, and a released tag can't be reused.
