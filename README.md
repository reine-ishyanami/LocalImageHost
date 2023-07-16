# LocalImageHost

> v3.0.0

Base URLs:

# Default

## POST 上传图片接口

POST /{project}

> Body 请求参数

```yaml
imgFile: string
filename: string
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|project|path|string| 是 |图片所属项目名称|
|body|body|object| 否 |none|
|» imgFile|body|string(binary)| 是 |上传的图片|
|» filename|body|string| 否 |none|

> 返回示例

> 成功

```json
{
  "success": true,
  "message": "上传成功",
  "data": {
    "project": "test",
    "filename": "file.png"
  }
}
```

```json
{
  "success": false,
  "message": "上传失败"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|[Result](#schemaresult)|

## GET 获取图片

GET /{project}/{imgName}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|project|path|string| 是 |图片所属项目名称|
|imgName|path|string| 是 |图片名称|

> 返回示例

> 成功

```json
{
  "success": false,
  "message": "图片展示失败"
}
```

```json
{
  "success": true,
  "message": "图片展示成功"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none|成功|none|
|» message|string|true|none|信息|none|

## DELETE 删除图片

DELETE /{project}/{imgName}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|project|path|string| 是 | 图片所属项目名称|
|imgName|path|string| 是 |图片名称|

> 返回示例

> 成功

```json
{
  "success": true,
  "message": "图片删除成功"
}
```

```json
{
  "success": false,
  "message": "图片删除失败"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» success|boolean|true|none|成功|none|
|» message|string|true|none|信息|none|

## GET 查询图片列表

GET /list

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|project|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{
  "success": true,
  "message": "string",
  "data": {
    "project": "string",
    "filename": "string",
    "list": [
      {
        "url": "string",
        "id": 0,
        "path": "string",
        "project": "string",
        "name": "string"
      }
    ]
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|成功|[Result](#schemaresult)|

# 数据模型

<h2 id="tocS_ImageWithUrl">ImageWithUrl</h2>

<a id="schemaimagewithurl"></a>
<a id="schema_ImageWithUrl"></a>
<a id="tocSimagewithurl"></a>
<a id="tocsimagewithurl"></a>

```json
{
  "url": "string",
  "id": 0,
  "path": "string",
  "project": "string",
  "name": "string"
}

```

图片信息

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|url|string|true|none||none|
|id|integer|true|none||none|
|path|string|true|none||none|
|project|string|true|none||none|
|name|string|true|none||none|

<h2 id="tocS_Result">Result</h2>

<a id="schemaresult"></a>
<a id="schema_Result"></a>
<a id="tocSresult"></a>
<a id="tocsresult"></a>

```json
{
  "success": true,
  "message": "string",
  "data": {
    "project": "string",
    "filename": "string",
    "list": [
      {
        "url": "string",
        "id": 0,
        "path": "string",
        "project": "string",
        "name": "string"
      }
    ]
  }
}

```

返回结果

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|success|boolean|true|none|成功|none|
|message|string|true|none|信息|none|
|data|object|false|none|数据|none|
|» project|string|false|none|项目名|none|
|» filename|string|false|none|文件名|none|
|» list|[[ImageWithUrl](#schemaimagewithurl)]|false|none||none|

