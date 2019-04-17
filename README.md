## O que é um Tasklet Batch Spring?
```
   No batch Spring, a Taskleté uma interface que executa uma única tarefa dentro de um Step. Um caso de uso típico para implementar um Taskleté a configuração ou limpeza de recursos antes ou depois da execução de um Step.

Na verdade, o Spring Batch oferece duas maneiras diferentes de implementar uma etapa de um trabalho em lotes : usando Chunks ou usando um Tasklet .

No exemplo do Spring Batch Job , vimos que um trabalho em lote consiste em um ou mais Steps. E Taskletrepresenta o trabalho que é feito em a Step.

A Taskletinterface tem um método: execute () . A Stepchama este método repetidamente até terminar ou lançar uma exceção.

A estrutura do Spring Batch contém algumas implementações da Taskletinterface. Um deles é um “processamento orientado por partes” Tasklet. Se você olhar para o ChunkOrientedTasklet você pode ver que ele implementa a Taskletinterface.

https://codenotfound.com/spring-batch-tasklet-example.html
```

## Teste
```
mvn test
```

## Target
```
Você pode encontrar o resultado no arquivo target / test-outputs / person.txt :

nome sobrenome
nome sobrenome
```
