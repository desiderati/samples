'use strict';

App.controller('QRCodeController', [
    function() {

        var self = this;

        self.generate = function() {
            console.log("Generating QR Code...");
            $("#qrCodeImage").html(
                "<img src='" + document.location.toString() + "api/v1/qrcode/generate?t=" + new Date().getTime() +  "'/>");
            console.log("QR Code Generated!");
        };
    }]
);

