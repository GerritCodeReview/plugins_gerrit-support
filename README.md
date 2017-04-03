# Gerrit-Support Plugin

Collect information on Gerrit Code Review in order to have enough
elements to request support.

## How to build

Gerrit-Support plugins is written in Scala language and built with Sbt.
To build the plugin you need to have SBT 0.13.13 installed and then
run the ``assembly`` target.

Example:

```
   $ sbt assembly
   [...]
   [info] Packaging /Users/lucamilanesio/gerrithub/gerrit-support/target/scala-2.11/gerrit-support.jar ...
```

## Usage

```
> curl -v -H "Content-Type: application/json" -d '{"gerritVersion":"true"}' http://localhost:8080/plugins/gerrit-support/collect

> POST /plugins/gerrit-support/collect HTTP/1.1

< HTTP/1.1 201 Created
< Location: /plugins/gerrit-support/collect/20170319-175459-collect-ab2093a4-139f-4310-872f-6d00bfa313e5.zip
```
The resulting zip file can be then downloaded using an HTTP GET.
Example:

```
curl -v http://localhost:8080/plugins/gerrit-support/collect/20170319-175459-collect-ab2093a4-139f-4310-872f-6d00bfa313e5.zip

> GET /plugins/gerrit-support/collect/20170319-175459-collect-ab2093a4-139f-4310-872f-6d00bfa313e5.zip

< HTTP/1.1 200 OK
< Content-Type: multipart/x-zip;charset=utf-8
```
The archive bundle contains one entry named 'version.json' with a
single JSON String containing the Gerrit version.

Example:

```
unzip 20170319-175459-collect-ab2093a4-139f-4310-872f-6d00bfa313e5.zip
  inflating: version.json
cat version.json
"2.13.6-3240-g1c96d0a"
```