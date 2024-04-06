var createError = require("http-errors");
var express = require("express");
var path = require("path");
var cookieParser = require("cookie-parser");
var logger = require("morgan");
var indexRouter = require("./routes/index");
//var apiRouter = require('./routers/api')

var usersRouter = require("./routes/userRoutes");
var distributorsRouter = require("./routes/distributorsRoutes");
var fruitsRouter = require("./routes/fruitsRoutes");
var uploadsRouter = require("./routes/uploadRoutes");
var emailRouter = require("./routes/emailRoutes");
var loginRouter = require("./routes/loginRoutes");

const database = require("./config/db");
var app = express();

// view engine setup
app.set("views", path.join(__dirname, "views"));
app.set("view engine", "hbs");

app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));

app.use("/", indexRouter);
//app.use('/api', apiRouter);
app.use("/users", usersRouter);
app.use("/distributors", distributorsRouter);
app.use("/fruits", fruitsRouter);
app.use("/uploads", uploadsRouter);
app.use("/sendmail", emailRouter);
app.use("/login", loginRouter);

database.connect();

// catch 404 and forward to error handler
app.use(function (req, res, next) {
  next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render("error");
});

module.exports = app;
