const path = require('path')
const express = require('express')
const expressHbs = require('express-handlebars')
const app = express()

app.use(express.json())
app.use(express.static(path.join(__dirname, 'public')))
app.engine('hbs', expressHbs.engine({
    extname: ".hbs",
    layoutsDir: path.join(__dirname, 'views/layouts'),
    defaultLayout: "index",
    helpers: {
        sum: (a) => a + 1,


    }
}));
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'))


app.get('/', (req, res) => {
    res.render('home')
})



app.listen(3000, () => {
    console.log('http://localhost:3000')
})