# Endpoints

## Ingredients

### GET /ingredients/find
Finds an ingredient by barcode. If the ingredient does not exist in the local database orr
the ingredient has not been refreshed for the last 2 hours, the API will update its local
database from https://fr.openfoodfacts.org
#### Example request
/ingredients/find?barcode=3029330003533
#### Parameters
| Name          | Type           | Example       |
| ------------- |:--------------:| -------------:|
| barcode     | long           | 3029330003533   |

#### Returns
[Ingredient](Ingredient.md) or [ErrorMessage](ErrorMessage.md)

#### Error codes


## Recipe

### POST /recipes/all
Returns all recipes from the authenticated user

#### Returns
List of [Recipes](Recipe.md) or [ErrorMessage](ErrorMessage.md)


#### Error codes


### PUT /recipes/find
Creates the recipe delivered in the body of the put request

#### Example request
/recipes/find?recipeId=84
#### Parameters
| Name          | Type           | Example       |
| ------------- |:--------------:| -------------:|
| recipeId      | int            | 84            |


#### Returns
[Recipe](Recipe.md) or [ErrorMessage](ErrorMessage.md)

#### Error codes


### PUT /recipes/create
Creates the recipe delivered in the body of the put request

#### Example body
```json
{
    "title": "Rød grød med fløde",
    "ingredients": [
        {
            "ingredientBarcode": 3029330003533,
            "quantity": 200,
            "idUnit": 1
        },
        {
            "ingredientBarcode": 5010477301246,
            "quantity": 456,
            "idUnit": 1
        }
    ]
}
```

#### Returns
OK response header or [ErrorMessage](ErrorMessage.md)


#### Error codes

### DELETE /recipes/delete
Deletes a recipe

#### Example request
/recipes/delete?recipeId=84
#### Parameters
| Name          | Type           | Example       |
| ------------- |:--------------:| -------------:|
| recipeId      | int            | 84            |


#### Returns
Ok return request or [ErrorMessage](ErrorMessage.md)


#### Error codes

### GET /recipes/serving
Calculates the ingredient amount for a certain amount of people for a certain recipe

#### Example request
/recipes/serving?recipeId=84&people=4
#### Parameters
| Name          | Type           | Example       |
| ------------- |:--------------:| -------------:|
| recipeId      | integer        | 84            |
| people        | integer        | 4             |

#### Returns
[Serving](Serving.md) or [ErrorMessage](ErrorMessage.md)

#### Error codes



## Units

### GET /units/all
Returns all units

#### Returns
List of [Units](Unit.md)

#### Error codes


### GET /units/find

#### Example request
/ingredients/find?id=1
#### Parameters
| Name          | Type           | Example       |
| ------------- |:--------------:| -------------:|
| id            | integer        | 1             |

#### Returns
[Unit](Unit.md) or [ErrorMessage](ErrorMessage.md)

#### Error codes