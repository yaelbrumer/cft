﻿using Accord.IO;
using Accord.Math;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CFT_Classification
{
    public partial class MainForm : Form
    {
        string[] columnNames; // stores the column names for the loaded data

        public MainForm()
        {
            InitializeComponent();
        }

        private void MenuFileOpen_Click(object sender, EventArgs e)
        {
            if (openFileDialog.ShowDialog(this) == DialogResult.OK)
            {
                string filename = openFileDialog.FileName;
                string extension = Path.GetExtension(filename);

                if (extension == ".xls" || extension == ".xlsx")
                {
                    ExcelReader db = new ExcelReader(filename, true, false);
                    //TODO: do we want to support multiple worksheets selection? 
                    DataTable tableSource = db.GetWorksheet(0);
                    double[,] sourceMatrix = tableSource.ToMatrix(out columnNames);
                    //var x  = tableSource.ToArray(out columnNames);

                    // Detect the kind of problem loaded.
                    if (sourceMatrix.GetLength(1) == 2)
                    {
                        MessageBox.Show("Missing class column.");
                    }
                    else
                    {
                        this.cftLearningSource.DataSource = tableSource;
                        lbStatus.Text = "Switch to the Machine Creation tab to create a learning machine!";
                    }
                }
                else
                {
                    MessageBox.Show("Error:\nFile format must be xls/xlsx.");
                }
            }
        }

    }
}
