Story: pesquisar palestrante

Scenario: deve pesquisar palestrantes pelo nome

Given palestrante de [nome]

When pesquiso palestrante

Then palestrantes encontrados igual a [total]


Examples:
|nome|total|
|Pestano|1|
|a|2|
|xyz|0|


