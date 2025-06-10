/*
 * Copyright (c) 2025 - Felipe Desiderati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
(function (app) {
  'use strict';

  const apiUrl = 'http://localhost:9090/sample-mongodb/'

  app.factory('CsrfService', ['$http', '$q', function ($http, $q) {
    return {
      /**
       * When logging in for the first time using form-based authentication,
       * the CSRF cookie is not set upon redirection to the main page.
       * This happens because Spring Security hasn’t had the chance to include
       * the CSRF token in a response body (HTML or JSON).
       * The token is only sent in responses with a body—not in 302 Found redirects.
       * By doing this, we force the CSRF token to be set right after login.
       */
      getCsrfToken: function () {
        return $http.get(apiUrl + 'csrf').then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while fetching CSRF Token.', errResponse);
            return $q.reject(errResponse.data);
          },
        );
      }
    }
  }]);

  app.controller('QRCodeController', ['CsrfService',
    function (CsrfService) {

      let self = this;
      self.generate = function () {
        console.log('Generating QR Code...');
        $('#qrCodeImage').html(
          '<img alt=\'QR Code\' src=\'' + apiUrl + 'api/v1/qrcode/generate?t=' + new Date().getTime() + '\'/>');
        console.log('QR Code Generated!');
      };

      CsrfService.getCsrfToken().then(
        () => console.log('CSRF Token fetched with success!')
      )
    }],
  );

})(app);
