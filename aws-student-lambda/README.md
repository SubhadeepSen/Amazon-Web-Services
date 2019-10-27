# Instruction

### Create API Gateway for mapping GET & POST request
### Integrate GET & POST request in Lambda Integration to pass the request information to handleRequest method

## GET

#set ($inputRoot = $input.path('$')){

"httpMethod" : "$context.httpMethod",

"id" : $input.params('id')

}

## POST

#set ($inputRoot = $input.path('$')){

"httpMethod" : "$context.httpMethod",

"student" : $input.json('$')

}
