const http = require('http');
const fs = require('fs');
const path = require('path');

const server = http.createServer((req, res) => {
    console.log(`Запрос: ${req.method} ${req.url}`);
    
    if (req.method === 'OPTIONS') {
        const options = {
            hostname: 'localhost',
            port: 8080,
            path: req.url,
            method: 'OPTIONS',
            headers: {
                'Origin': 'http://localhost:3000',
                'Access-Control-Request-Method': req.headers['access-control-request-method'],
                'Access-Control-Request-Headers': req.headers['access-control-request-headers']
            }
        };
        
        const proxyReq = http.request(options, (proxyRes) => {
            res.writeHead(proxyRes.statusCode, proxyRes.headers);
            proxyRes.pipe(res);
        });
        
        proxyReq.on('error', (e) => {
            console.error('Proxy error:', e);
            res.writeHead(502);
            res.end();
        });
        
        proxyReq.end();
        return;
    }
    
    if (req.url.startsWith('/api/')) {
        const options = {
            hostname: 'localhost',
            port: 8080,
            path: req.url,
            method: req.method,
            headers: {
                ...req.headers,
                host: 'localhost:8080'
            }
        };
        
        delete options.headers['connection'];
        
        const proxyReq = http.request(options, (proxyRes) => {
            res.writeHead(proxyRes.statusCode, proxyRes.headers);
            proxyRes.pipe(res);
        });
        
        proxyReq.on('error', (e) => {
            console.error('Proxy error:', e);
            res.writeHead(502, { 'Content-Type': 'text/plain; charset=utf-8' });
            res.end('Bad Gateway');
        });
        
        if (req.method === 'POST' || req.method === 'PUT') {
            req.pipe(proxyReq);
        } else {
            proxyReq.end();
        }
        
        return;
    }
    
    const protectedPages = ['/hello.html', '/index.html', '/main.html', '/dashboard.html'];
    const requestedPage = req.url.toLowerCase();
    
    if (protectedPages.some(page => requestedPage === page || requestedPage === page.replace('.html', ''))) {
        console.log('Запрос защищенной страницы, проверяем авторизацию...');
        
        const authOptions = {
            hostname: 'localhost',
            port: 8080,
            path: '/api/auth/session',
            method: 'GET',
            headers: {
                'Cookie': req.headers.cookie || '',
                'Content-Type': 'application/json'
            }
        };
        
        const authReq = http.request(authOptions, (authRes) => {
            let data = '';
            
            authRes.on('data', (chunk) => {
                data += chunk;
            });
            
            authRes.on('end', () => {
                try {
                    const result = JSON.parse(data);
                    console.log('Результат проверки авторизации:', result);
                    
                    if (result.authenticated === true) {
                        console.log('Авторизация успешна, отдаем страницу');
                        serveStaticFile(req.url, res);
                    } else {
                        console.log('Пользователь не авторизован, редирект на login');
                        res.writeHead(302, { 
                            'Location': '/login.html',
                            'Set-Cookie': authRes.headers['set-cookie'] || ''
                        });
                        res.end();
                    }
                } catch (error) {
                    console.error('Ошибка парсинга ответа:', error);
                    res.writeHead(302, { 'Location': '/login.html' });
                    res.end();
                }
            });
        });
        
        authReq.on('error', (error) => {
            console.error('Ошибка проверки авторизации:', error);
            res.writeHead(302, { 'Location': '/login.html' });
            res.end();
        });
        
        authReq.end();
        return;
    }
    
    serveStaticFile(req.url, res);
});

function serveStaticFile(url, res) {
    let filePath = '.' + url;
    if (filePath === './') {
        filePath = './login.html';
    }
    
    filePath = path.normalize(filePath).replace(/^(\.\.[/\\])+/, '');
    
    const ext = path.extname(filePath);
    const contentType = getContentType(ext);
    
    fs.readFile(filePath, (err, data) => {
        if (err) {
            res.writeHead(404, { 
                'Content-Type': 'text/html; charset=utf-8'
            });
            res.end(`
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>404 - Страница не найдена</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                            height: 100vh;
                            margin: 0;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            color: white;
                            text-align: center;
                        }
                        .container {
                            background: rgba(255, 255, 255, 0.1);
                            padding: 40px;
                            border-radius: 10px;
                            backdrop-filter: blur(10px);
                            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
                        }
                        h1 {
                            font-size: 72px;
                            margin: 0;
                            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
                        }
                        p {
                            font-size: 24px;
                            margin: 20px 0;
                        }
                        a {
                            display: inline-block;
                            padding: 10px 30px;
                            background: white;
                            color: #667eea;
                            text-decoration: none;
                            border-radius: 5px;
                            font-weight: bold;
                            transition: transform 0.3s;
                        }
                        a:hover {
                            transform: scale(1.05);
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>404</h1>
                        <p>Страница не найдена</p>
                        <a href="/login.html">Вернуться на главную</a>
                    </div>
                </body>
                </html>
            `);
        } else {
            res.writeHead(200, { 
                'Content-Type': contentType.includes('text/') 
                    ? contentType + '; charset=utf-8' 
                    : contentType
            });
            res.end(data);
        }
    });
}

function getContentType(ext) {
    const types = {
        '.html': 'text/html',
        '.css': 'text/css',
        '.js': 'text/javascript',
        '.json': 'application/json',
        '.png': 'image/png',
        '.jpg': 'image/jpeg',
        '.gif': 'image/gif',
        '.ico': 'image/x-icon'
    };
    return types[ext] || 'text/plain';
}

server.listen(3000, () => {
    console.log('Фронтенд сервер запущен на http://localhost:3000');
    console.log('Проксирование API запросов на http://localhost:8080');
    console.log('Защищенные страницы:', 'hello.html');
    console.log('Все CORS заголовки теперь устанавливаются бэкендом');
});