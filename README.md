# Это репозиторий проекта "Таск-менеджер"

---

## Описание

Приложение предназначено для управления и отслеживания задач. Оно позволяет пользователям создавать, редактировать и удалять задачи, а также получать информацию о них

## Что делает приложение:

1. Создает, изменяет и удаляет **простые задачи, большие задачи и позадачи**;
2. Самостоятельно обновляет статусы больших задач в зависимости от статусов ее подзадач; 
3. Предоставляет списки задач;
4. Сохраняет задачи и историю просмотров в файл;
5. Сохраняет для задач время старта, окончания и продолжительности задач;
6. Проверяет, чтобы задачи не пересекались по времени;
7. Выводит список задач по приоритету.
8. Умеет принимать и обрабатывать внешние запросы и сохранять задачи и историю просмотров на сервер.


## Технологический стек

Проект использует следующий набор технологий, библиотек и инструментов:

- **[Amazon Corretto 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html)**
- **[JUnit 5](https://junit.org/junit5/)**
- **[GSON](https://github.com/google/gson)**


## Установка и запуск

Для того чтобы запустить проект у себя локально, необходимо выполнить следующие шаги:

### Предварительные требования

Убедитесь, что на вашем компьютере установлены следующие инструменты:

- Java JDK 11 или выше: [Скачать](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- IntelliJ IDEA (для разработки): [Скачать](https://www.jetbrains.com/idea/download/)

### Установка проекта

1. Клонируйте репозиторий проекта:

```
git clone https://https://github.com/DmitryMisevra/java-canban.git
cd java-canban
```

2. Соберите проект в intellij Idea:

1. Откройте проект в IntelliJ IDEA.
2. Настройте создание JAR файла через `File > Project Structure > Artifacts`.
3. Соберите артефакт через `Build > Build Artifacts...`.

Чтобы запустить приложение, выполните следующую команду:

```
java -jar path/to/your/filename.jar
```

Замените path/to/your/filename.jar на актуальный путь к вашему исполняемому файлу JAR.