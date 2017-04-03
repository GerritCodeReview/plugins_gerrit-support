gerrit-support REST API
==============================

NAME
----
collect/1 - Prepare a .zip file with various support information

SYNOPSIS
--------
>     POST /plugins/gerrit-support/collect

DESCRIPTION
-----------
Prepares a zip file collecting information requested in the json request


EXAMPLES
--------

Ask the server to prepare zip file for version and cpuinfo

>     curl -v -H "Content-Type: application/json" \
>        -d '{"gerritVersion":"true","cpuInfo":"true" }' \
>        http://host:port/plugins/gerrit-support/collect

```
< HTTP/1.1 201 Created
< Date: Tue, 04 Apr 2017 22:53:33 GMT
< Content-Type: text/plain; charset=UTF-8
< Location: /plugins/gerrit-support/collect/20170405-005334-collect-b6d2bc6a-7f01-4b93-9f74-ad28b4a68e67.zip
< Content-Length: 2

```
NOTE: Location header gives the name of the prepared file to be specified
in next call.

NAME
----
collect/2 - Download the requested information

SYNOPSIS
--------
>     GET /plugins/gerrit-support/collect/<zip file name>

DESCRIPTION
-----------
Gets the file prepared with previous call 


EXAMPLES
--------

Obtain the prepared file into a local file

>     curl http://host:port/plugins/gerrit-support/collect/20170405-005334-collect-b6d2bc6a-7f01-4b93-9f74-ad28b4a68e67.zip \
>          -o received.zip

