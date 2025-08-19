package com.agn1kobi.e_commerce_backend.common.types;

public record RequestResult<Response>(Result result, Response response) {
}
