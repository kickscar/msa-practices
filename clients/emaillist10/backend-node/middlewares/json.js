exports.acceptOnlyJsonRequest = (req, res, next) => {
    /* if request with accept html */
    if (req.accepts('html')) {
        return res.status(400).end();
    }

    /* if request with accept json */
    next?.();
}

exports.jsonResult = (req, res, next) => {
    try {
        const jsonResult = (obj) => obj instanceof Error ? {
            result: 'fail',
            data: null,
            message: obj.message
        } : {
            result: 'success',
            data: obj,
            message: null
        };

        const jsonOriginalFn = res.json;

        res.json = (param) => {
            if (param && param.then != undefined) {                     // Async Call
                return param.then((obj) => {
                    res.json = jsonOriginalFn;
                    return jsonOriginalFn.call(res, jsonResult(obj));
                }).catch((error) => {
                    next?.(error);
                });
            } else {                                                    // Non-Async Call
                res.json = jsonOriginalFn;
                return jsonOriginalFn.call(res, jsonResult(param));
            }
        }

        next?.();

    } catch (error) {
        next?.(error);
    }
}