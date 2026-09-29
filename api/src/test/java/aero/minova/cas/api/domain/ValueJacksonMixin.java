package aero.minova.cas.api.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonDeserialize(using = ValueJacksonDeserializer.class)
@JsonSerialize(using = ValueJacksonSerializer.class)
public abstract class ValueJacksonMixin {

}
