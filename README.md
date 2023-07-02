# Dynamic Authorization Demo

This demo bases on RBAC.

For a resource, it is identified by method and path. Its attribute `auth_type` has 4 value: `PERMIT_ALL`, `JUST_AUTHENTICATION`, `ANY_ROLE`, and `ALL_ROLE`.

For `PERMIT_ALL`, it means this resource can be visited by any requester.

For `JUST_AUTHENTICATION`, it means this resource can be visited by any authenticated requesters.

For `ANY_ROLE`, it means this resource can be visited by requesters having a role set that include at least one role from the role set of resource.

For `ALL_ROLE`, it means this resource can be visited by requesters having a role set that include all roles from the role set of resource.

A custom annotation `@PermitAll` is defined for convienience in setting resources that permit all request. This annotation will be used before endpoint handler methods in `Controller`. There is an assumption each handler method in `Controller` will have request method declaration.

This demo integrated with Spring Security for authentication and authorization.

For authentication, there is a JWT filter added into the filter chain.

For authorization, there is a custom authentication manager that retrieve roles of current requester and resource from database and do comparison according to the `auth_type` of resource.

Therefore, there is no need to do modification to the Spring Security configuration class since configuring authorities for resources.

The old version is in the folder `_old`. It depends on two interceptors to do authentication and authorization.