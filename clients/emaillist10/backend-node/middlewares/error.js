exports.error404 = (req, res) => {
    /* if request with accept html */
    if (req.accepts('html')) {
        return res.render('index');
    }

    /* if request with accept json */
    res.status(404).json(new Error('unknown request'));
};

exports.error500 = (error, req, res, next) => {

    /* if request with accept html */
    if (req.accepts('html')) {
        return res.status(error.status || 500).render('error/500', {
            error: error.stack
        });
    }

    /* if request with accept json */
    res.status(500).json(new Error(error.stack));
}