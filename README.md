# CompletableFuture Lab

Небольшая лабораторная работа на Java, демонстрирующая, как `CompletableFuture` помогает собирать независимые асинхронные операции в один результат, обрабатывать ошибки и выбирать executor для CPU-bound нагрузки.

## Установка и запуск

Нужны JDK 26 и Maven 3.9+.

```bash
git clone <repository-url>
cd CompletableFuture_Lab
mvn compile
```

Запуск примеров:

```bash
java -cp target/classes org.Lab_Main
java -cp target/classes org.ThreadOutput_Main
java -cp target/classes org.BlockingHttp_Main
java -cp target/classes org.CpuBoundMain
```

Описание экспериментов — в [notes/lab_work.md](notes/lab_work.md).
