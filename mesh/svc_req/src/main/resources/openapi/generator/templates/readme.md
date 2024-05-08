## Что в этой директории?
Кастомизированные шаблоны для openapi-generator-maven-plugin версии 7.5.0

## Зачем?
Шаблоны позволяют разметить сгенерированный код (интерфейс контроллера и DTO) аннотациями

## Как заставить плагин использовать шаблоны из данной директории?
Указать директорию в параметре 'templateDirectory' конфигурации плагина.

### Я хочу обновить версию плагина. Нужно ли обновлять шаблоны?
Если шаблон modules\openapi-generator\src\main\resources\java-helidon\server\libraries\mp\RestApplication.mustache
 в новой версии обновились, то нужно заменить его содержимое на:

public class RestApplication {

}
Иначе тесты будут запускать несконфигурированный сервер.

Также нужно обновить шаблон src/main/resources/openapi/generator/templates/7.5.0/client/library/mp/api.mustache
в него были добавлены:

import {{rootJavaEEPackage}}.ws.rs.core.MediaType;

+ import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;

{{/appName}}
+ @RegisterClientHeaders
@RegisterRestClient{{#configKey}}(configKey="{{configKey}}"){{/configKey}}

Добавление @RegisterClientHeaders добавляет проброску полей из httpHeader в restClient
Поля прописываются в org.eclipse.microprofile.rest.client.propagateHeaders
К сожалению приходится копировать все шаблоны, копирование одного как для сервера не работает :(
#
