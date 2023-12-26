exports.resLocals = (req, res, next) => {
    res.locals.req = req;
    res.locals.res = res;
    next?.();
};