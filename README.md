# version-check-service

[![Clojars Project](https://img.shields.io/clojars/v/org.openvoxproject/dujour-version-check.svg)](https://clojars.org/org.openvoxproject/dujour-version-check)

This library allows you to perform version checks with dujour. To use this in your project,
add the following to your `project.clj` file:

```
[org.openvoxproject/dujour-version-check "0.1.2"]

```

_Note:_ The latest version number of this library can be found above

Then, call the `check-for-updates!` function. This function takes two arguments,
`request-values` and `update-server-url`. `update-server-url` should be a string
containing the URL of the update server. `request-values` is a map that currently only
supports a single key, `:product-name`. The value contained at this key can either be a string
containing the artifact-id or a map with the following schema:

```clj
{:group-id schema/Str
 :artifact-id schema/Str}
```

If only the artifact id is provided, the group id will default to
`"puppetlabs.packages"`.

The request map can also accept `:certname` and `:cacert` strings, which
can be used to uniquely identify a user. These values will be SHA-512
hashed and sent as `site-id` and `host-id`, respectively, before being
sent to the server.

This library provides one other public API function, `get-version-string`. This function
takes one argument, `product-name`, which should be the artifact id as a string. It
optionally takes one more argument, `group-id`, which should be the group-id of the
desired artifact as a string.

## License

Copyright © 2014 Puppet Labs

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
