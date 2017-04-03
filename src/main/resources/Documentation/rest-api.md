gerrit-support REST API
==============================

NAME
----
collect/1 - Prepare a support .zip file

SYNOPSIS
--------
>     POST /plugins/gerrit-support/collect

DESCRIPTION
-----------
Prepares a zip file collecting information requested in the JSON request.

The JSON payload contains a series of flags with the list of information to be
collected and archived in the .zip file.

- gerritVersion - JSON String with the version of running Gerrit server
- cpuInfo - JSON Object with all the CPU information collected by [jHardware](https://github.com/profesorfalken/jHardware)


EXAMPLES
--------

Ask the server to prepare zip file for version and cpuinfo

>     curl -v -H "Content-Type: application/json" \
>        -d '{"gerritVersion": true,"cpuInfo": true }' \
>        http://host:port/plugins/gerrit-support/collect

```
< HTTP/1.1 201 Created
< Date: Tue, 04 Apr 2017 22:53:33 GMT
< Content-Type: text/plain; charset=UTF-8
< Location: /plugins/gerrit-support/collect/20170405-005334-collect-b6d2bc6a-7f01-4b93-9f74-ad28b4a68e67.zip
< Content-Length: 2

```
NOTE: Location header gives the name of the prepared file created on Gerrit server.

NAME
----
collect/2 - Download a support file

SYNOPSIS
--------
>     GET /plugins/gerrit-support/collect/<zip file name>

DESCRIPTION
-----------
Download a Gerrit support .zip file previously prepared.

EXAMPLES
--------

Download the .zip support file

>     curl http://host:port/plugins/gerrit-support/collect/20170405-005334-collect-b6d2bc6a-7f01-4b93-9f74-ad28b4a68e67.zip \
>          -o received.zip

