# DynamicAuthorityDemo

For a resource including request method and path, a user having specific roles and authorities can request.

Integrating JWT in the authentication.

A resource with roles and authorities can be visited by users with all these roles and authorities or just have any of them. An attribute of a resource will define this resource requiring all or any.

All roles and authorities are tags for users and resources.

For example:

There are roles: task, group.

There are authorities: create, delete, update, and query.

Role group has the authority to create and delete, but role task does not have any authority.

An example user has a role task and group and has an authority query. Tags of this user will be a set of task, create, delete, and query, which means if a role has authorities, its authorities will become tags, but it will not.

During verifying whether a user can visit a resource, 

if this resource requires all roles and authorities, the tag set of this user needs to contain all tags from the resource tag set

if this resource requires any roles and authorities, the tag set of this user needs to contain one of the tags from the resource tag set

There is a front-end portal for testing. The initial account is `default / 123qweasd`.

The ERD is in README.md of the back-end fold.