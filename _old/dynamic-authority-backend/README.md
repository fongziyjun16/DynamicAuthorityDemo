## Dynamic Authority ERD
```mermaid
---
title: Dynamic Authority ERD
---
erDiagram
    user {
        int id
        varchar username
        varchar password
    }

    user_role {
        int user_id
        int role_id
    }

    role {
        int id
        varchar name
    }

    role_authority {
        int role_id
        int authority_id
    }

    authority {
        int id
        varchar name
    }

    user_authority {
        int user_id
        int authority_id
    }

    user ||--|{ user_role: ""
    role ||--|{ user_role: ""
    role ||--|{ role_authority: ""
    authority ||--|{ role_authority: ""
    user ||--|{ user_authority: ""
    authority ||--|{ user_authority: ""

    resource {
        int id
        varchar url
        varchar method
        int permit_all "0(default) - authentication(must) and authorization(maybe), 1 - all free to go"
        int authorization_type "0(default) - no need authorization, 1 - any, 2 - all. only work when permit_all is 0"
    }

    resource_role {
        int resource_id
        int role_id
    }

    resource_authority {
        int resource_id
        int authority_id
    }

    resource ||--o{ resource_role: ""
    role ||--o{ resource_role: ""
    resource ||--o{ resource_authority: ""
    authority ||--o{ resource_authority: ""
```

## Assumption

Each endpoint in each controller will be annotated with http request method.

Each endpoint will be identified by a path and a request method.

## Steps

After the application started, a database called `dynamic_authority` will be created and all endpoints information will
be stored in this database.