using CefSharp;
using CefSharp.WinForms;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp122
{
    public partial class Form1 : Form
    {

        ChromiumWebBrowser browser;
        public Form1()
        {
            InitializeComponent();
            browser = new ChromiumWebBrowser("http://ya.ru");
            browser.FrameLoadEnd += WebBrowserFrameLoadEnded;
            browser.JavascriptMessageReceived += WebBrowserFrameJS;
            browser.LoadingStateChanged += WebBrowserState;
            browser.Dock = DockStyle.Fill;
            this.panel1.Controls.Add(browser);
            browser.Load("file:///D:/1.html");

            CefSharpSettings.WcfEnabled = true;
            browser.JavascriptObjectRepository.Settings.LegacyBindingEnabled = true;
            browser.JavascriptObjectRepository.Register("callbackObj", new CallbackObjectForJs(), isAsync: false, options: BindingOptions.DefaultBinder);   
        }

        private void WebBrowserState(object sender, LoadingStateChangedEventArgs e)
        {
            Console.WriteLine("1");
        }

        private void WebBrowserFrameJS(object sender, JavascriptMessageReceivedEventArgs e)
        {
            if (e.Frame.IsMain)
            {
                browser.ViewSource();
                browser.GetSourceAsync().ContinueWith(taskHtml =>
                {
                    var html = taskHtml.Result;
                });
            }
        }

        private void WebBrowserFrameLoadEnded(object sender, FrameLoadEndEventArgs e)
        {
            string script = "input = document.getElementById('txt');\n"+
                "input.addEventListener('change', function(){callbackObj.showMessage(this.value);});";
            browser.EvaluateScriptAsync(script);
        }

        public class CallbackObjectForJs
        {
            public void showMessage(string msg)
            {
                MessageBox.Show(msg);
            }
        }
    }
}
