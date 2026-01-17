const { app, BrowserWindow } = require('electron');

let mainWindow;

function createWindow() {
    mainWindow = new BrowserWindow({
        width: 1280,
        height: 800,
        show: false,
        webPreferences: {
            contextIsolation: true,
            nodeIntegration: false,
        },
    });

    mainWindow.once('ready-to-show', () => {
        mainWindow.show();
    });

    const url =
        process.env.NODE_ENV === 'development'
            ? 'http://localhost:3000'
            : 'http://localhost:3000';

    mainWindow.loadURL(url);
}

app.whenReady().then(createWindow);

app.on('window-all-closed', () => {
    app.quit();
});
