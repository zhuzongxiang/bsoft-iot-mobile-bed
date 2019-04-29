window.onload = function () {

    $env.defaultLoader = "web-vue2";
    $env.appPlatform = 143;

    $create("/ssdev.ux.WebLoader").then(function (loader) {
        loader.on("moduleLoaded",function (m) {
            $event.emit("moduleLoaded",m);
        });
        loader.on("moduleLoadError",function (e) {
            $event.emit("moduleLoadError",e);
            console.error(e);
        });
        loader.init();
    },function (e) {
        console.error(e);
        $event.emit("moduleLoadError",e);
    });
};