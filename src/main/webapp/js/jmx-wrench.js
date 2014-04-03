var Wrench = (function ($) {


  var Templates = undefined;

  function addMessage(compiledTemplate, data) {
    $('#messages').append(compiledTemplate(data));
  }

  function initTypeahead(servletPrefix) {
    Handlebars.registerHelper('wrappableName', function(name) {
      // insert a zero-width (=invisible) space after each colon
      // so the attributes get wrapped in the typeahead
      return name.split(',').join(',\u200B');
    });
    var engine = new Bloodhound({
      name: 'search',
      remote:  {
        url:  servletPrefix + "search?q=%QUERY",
        filter: function (parsedResponse) {
          // unwrap
          return parsedResponse.suggestions;
        }
      },
      datumTokenizer: function(d) {
        var res = Bloodhound.tokenizers.nonword(d);
        return res;
      },
      queryTokenizer: Bloodhound.tokenizers.whitespace
    });
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
    // Ajax submissions for setting properities and invoking methods
    // The structure of the responses is intentionally the same
    $("form.setpropertyform,form.setattributeform").submit(function(e)
    {
      var postData = $(this).serializeArray();
      var formURL = $(this).attr("action");
      $.post(formURL,postData, function(data) {
        addMessage(Templates.okayMessage, data);
      }).fail(function(jqXHR) {
        addMessage(Templates.errorMessage, $.parseJSON(jqXHR.responseText));
      });
      e.preventDefault(); //STOP default action
    });
  }

  function maybeCompile(elem) {
    var html = elem.html();
    return html ? Handlebars.compile(html) : null;
  }

  // Object "constructor" for public functions. Must be at the bottom
  return {
    init: function(apiContext) {
      // at the point of the execution of this script, the document is not fully loaded yet.
      Templates = {
        okayMessage: maybeCompile($('#okay-message')),
        errorMessage: maybeCompile($('#error-message')),
        suggestionT:  Handlebars.compile('<p><a href="{{url}}">{{{wrappableName name}}}</a></p>')
      }
      //Message.add(Templates.okayMessage, {message:"test"});
      bindSetProperty();
      // Tooltip-plugin
      $("*[data-toggle='tooltip']").tooltip();
      $(".collapse").on('show.bs.collapse', function (){
        var a = $(this).next("a");
        a.html("(Less&#x2026;)");
      });
      $(".collapse").on('hidden.bs.collapse', function (){
        var a = $(this).next("a");
        a.html("(More&#x2026;)");
      });
      initTypeahead(apiContext);
    }
  };
})(jQuery);