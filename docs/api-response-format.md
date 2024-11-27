---
title: API response format
---

## JSON Objects returned by API

Make sure the right content type like `Content-Type: application/json; charset=utf-8` is correctly returned.

### Users (for authentication)

```JSON
{
  "name": "Jake Jakeson",
  "email": "jake@jake.jake",
  "token": "jwt.token.here",
  "username": "jake",
  "createdAt": "2024-11-17T04:21:02.387+00:00"
}
```

### Profile

```JSON
{
  "name": "Jake Jakeson",
  "username": "jake",
}
```

### Single Tourist Spot

```JSON
{
  "slug": "how-to-train-your-dragon",
  "name": "How to train your dragon",
  "description": "Ever wonder how?",
  "typeList": ["dragons", "training"],
  "createdAt": "2016-02-18T03:22:56.637Z",
  "updatedAt": "2016-02-18T03:48:35.824Z",
  "averageRating": 4.4
}
```

### Multiple Tourist Spots

- `GET /api/tourist-spots`
- `GET /api/tourist-spots/feed`

```JSON
{
  "tourist-spots": [
    {
      "slug": "how-to-train-your-dragon",
      "name": "How to train your dragon",
      "description": "Ever wonder how?",
      "typeList": [
        "dragons",
        "training"
      ],
      "createdAt": "2016-02-18T03:22:56.637Z",
      "updatedAt": "2016-02-18T03:48:35.824Z",
      "averageRating": 4.4
    },
    {
      "slug": "how-to-train-your-dragon",
      "name": "How to train your dragon",
      "description": "Ever wonder how?",
      "typeList": [
        "dragons",
        "training"
      ],
      "createdAt": "2016-02-18T03:22:56.637Z",
      "updatedAt": "2016-02-18T03:48:35.824Z",
      "averageRating": 4.4
    }
  ],
  "itemCount": 2
}
```

### Single Comment

```JSON
{
  "id": 1,
  "createdAt": "2016-02-18T03:22:56.637Z",
  "body": "It takes a Jacobian",
  "author": {
    "username": "jake",
    "image": "https://i.stack.imgur.com/xHWG8.jpg",
  }
}
```

### Multiple Comments

```JSON
[{
  "id": 1,
  "createdAt": "2016-02-18T03:22:56.637Z",
  "body": "It takes a Jacobian",
  "author": {
    "username": "jake",
    "bio": "I work at statefarm",
    "image": "https://i.stack.imgur.com/xHWG8.jpg",
    "following": false
  }
}]
```

### Single Rating

```JSON
{
  "id": 1,
  "value": 4.3,
  "createdAt": "2016-02-18T03:22:56.637Z",
}
```

### List of Types

```JSON
{
  "types": [
    "reactjs",
    "angularjs"
  ]
}
```
