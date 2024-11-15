---
title: Endpoints
---

### Authentication Header

You can read the authentication header from the headers of the request

`Authorization: Token jwt.token.here`

### Authentication

`POST /api/users/login`

Example request body:

```JSON
{
  "user":{
    "email": "jake@jake.jake",
    "password": "jakejake"
  }
}
```

No authentication required, returns a [User](./api-response-format.md#users-for-authentication)

Required fields: `email`, `password`

### Registration

`POST /api/users`

Example request body:

```JSON
{
  "user":{
    "username": "Jacob",
    "email": "jake@jake.jake",
    "password": "jakejake"
  }
}
```

No authentication required, returns a [User](./api-response-format.md#users-for-authentication)

Required fields: `email`, `username`, `password`

### Get Current User

`GET /api/user`

Authentication required, returns a [User](./api-response-format.md#users-for-authentication)

### Get Profile

`GET /api/profiles/:username`

Authentication optional, returns a [Profile](./api-response-format.md#profile)

### List Tourist Spot

`GET /api/tourist-spots`

Returns most recent tourist-spots globally by default, provide `type` or `paid` query parameter to filter results

Query Parameters:

Filter by tag:

`?type=restaurant`

Filter by paid:

`?paid=true`

Limit number of tourist-spots (default is 20):

`?limit=20`

Offset/skip number of tourist-spots (default is 0):

`?offset=0`

Authentication optional, will return [multiple tourist-spots](./api-response-format.md#multiple-tourist-spots), ordered by most recent first

### Feed Tourist Spot

`GET /api/tourist-spots/feed`

Can also take `limit` and `offset` query parameters like List Tourist Spot.

Authentication required, will return [multiple tourist-spots](./api-response-format.md#multiple-tourist-spots) based on best ratings.

### Get tourist-spot

`GET /api/tourist-spots/:slug`

No authentication required, will return [single tourist-spot](./api-response-format.md#single-tourist-spot)

### Create tourist-spot

`POST /api/tourist-spots`

Example request body:

```JSON
{
  "tourist-spot": {
    "name": "Pika de Brennand",
    "gmaps-link": "https://batata.com"
    "description": "Ever wonder how?",
    "typeList": ["praca"],
    "paid": false
  }
}
```

Authentication required, will return an [tourist-spot](./api-response-format.md#single-tourist-spot)

Required fields: `name`, `gmaps-link`, `description`, `paid`

Optional fields: `typeList` as an array of Strings

### Update tourist-spot

`PUT /api/tourist-spots/:slug`

Example request body:

```JSON
{
  "tourist-spot": {
    "title": "Did you train your dragon?"
  }
}
```

Authentication required, returns the updated [tourist-spot](./api-response-format.md#single-tourist-spot)

Required fields: `name`, `gmaps-link`, `description`, `paid`, `typeList`

The `slug` also gets updated when the `name` is changed

### Delete tourist-spot

`DELETE /api/tourist-spots/:slug`

Authentication required

### Add Comments to an tourist-spot

`POST /api/tourist-spots/:slug/comments`

Example request body:

```JSON
{
  "comment": {
    "body": "His name was my name too."
  }
}
```

Authentication required, returns the created [Comment](./api-response-format.md#single-comment)

Required field: `body`

### Get Comments from an tourist-spot

`GET /api/tourist-spots/:slug/comments`

Authentication optional, returns [multiple comments](./api-response-format.md#multiple-comments)

### Delete Comment

`DELETE /api/tourist-spots/:slug/comments/:id`

Authentication required

### Add Rating to an tourist-spot

`POST /api/tourist-spots/:slug/ratings`

Example request body:

```JSON
{
  "rating": {
    "value": 4.3
  }
}
```

Authentication required, returns the created [Tag](./api-response-format.md#single-rating)

Required field: `value`

### Delete Rating

`DELETE /api/tourist-spots/:slug/ratings/:id`

Authentication required

### Get Types

`GET /api/types`

No authentication required, returns a [List of Types](./api-response-format.md#list-of-types)
