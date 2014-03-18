var Wrench = (function ($) {
  // Object "constructor" for public functions
  return {
    init: function(apiContext) {
      // at the point of the execution of this script, the document is not fully loaded yet.
      Templates = {
        okayMessage: Handlebars.compile($('#okay-message').html()),
        errorMessage: Handlebars.compile($('#error-message').html()),
        suggestionT:  Handlebars.compile('<p><a href="{{url}}">{{{wrappableName name}}}</a></p>')
      }
      //Message.add(Templates.okayMessage, {message:"test"});
      bindSetProperty();
      initTypeahead(apiContext);
    }
  }


  var Templates = undefined;

  var Message = {
    add: function(compiledTemplate, data) {
      $('#messages').append(compiledTemplate(data))
    }
  }

  function initTypeahead(servletPrefix) {
    console.log("init typeahead:" + servletPrefix);
    Handlebars.registerHelper('wrappableName', function(name) {
      // insert a zero-width (=invisible) space after each colon
      // so the attributes get wrapped in the typeahead
      return name.split(',').join(',\u200B');
    });
    var engine = new Bloodhound({
      name: 'search',
      remote:  {
        url:  servletPrefix + "?q=%QUERY",
        filter: function (parsedResponse) {
          // unwrap
          return parsedResponse.suggestions;
        }
      },
      datumTokenizer: function(d) {
        var res = Bloodhound.tokenizers.nonword(d);
        console.log(res);
        return res;
      },
      queryTokenizer: Bloodhound.tokenizers.whitespace
    });
    console.log($('#okay-message').html());
    engine.initialize();

    $('#search').typeahead({
      minLength: 2,
      highlight: true
    }, {
      source: engine.ttAdapter(),
      displayKey: 'name',
      templates: {
        empty: [
          '<div class="empty-message">',
          'No classes found',
          '</div>'
        ].join('\n'),
        suggestion: Templates.suggestionT
      }
    });
  }

  function bindSetProperty () {
// Ajax submissions for setting properities
//callback handler for form submit
    $("form.setpropertyform").submit(function(e)
    {
      var postData = $(this).serializeArray();
      var formURL = $(this).attr("action");
      $.ajax(
        {
          url : formURL,
          type: "GET",
          data : postData,
          success: function(data, textStatus, jqXHR)
          {
            Message.add(Templates.okayMessage, data);
          },
          error: function(jqXHR, textStatus, errorThrown)
          {
            Message.add(Templates.errorMessage, data);
          }
        });
      e.preventDefault(); //STOP default action
      e.unbind(); //unbind. to stop multiple form submit.
    });
  }


})(jQuery);